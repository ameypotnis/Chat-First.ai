package org.chatfirstai;

import io.micrometer.observation.ObservationRegistry;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.chatfirstai.data.PageEmbedding;
import org.chatfirstai.data.PageEmbeddingRepository;
import org.chatfirstai.data.PageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.DefaultChatClientBuilder;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mistralai.MistralAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConnectorController {
  private static final Logger log = LoggerFactory.getLogger(ConnectorController.class);
  private final PageRepository pageRepository;
  private final PageEmbeddingRepository pageEmbeddingRepository;
  private final MarkdownParser markdownParser;
  private final MistralAiEmbeddingModel mistralAiEmbeddingModel;
  private final OpenAiEmbeddingModel openAiEmbeddingModel;
  private final ChatClient mistralChatClient;
  private final ChatClient openaiChatClient;
  private final RestTemplate restTemplate;
  private final Environment environment;

  public ConnectorController(
      PageRepository pageRepository,
      PageEmbeddingRepository pageEmbeddingRepository,
      MarkdownParser markdownParser,
      OpenAiEmbeddingModel openAiEmbeddingModel,
      MistralAiEmbeddingModel mistralEmbeddingModel,
      @Qualifier("mistralAiChatModel") ChatModel mistralAiChatModel,
      @Qualifier("openAiChatModel") ChatModel openAiChatModel,
      Environment environment) {
    this.pageRepository = pageRepository;
    this.pageEmbeddingRepository = pageEmbeddingRepository;
    this.markdownParser = markdownParser;
    this.openAiEmbeddingModel = openAiEmbeddingModel;
    this.mistralAiEmbeddingModel = mistralEmbeddingModel;
    this.mistralChatClient =
        new DefaultChatClientBuilder(mistralAiChatModel, ObservationRegistry.NOOP, null).build();
    this.openaiChatClient =
        new DefaultChatClientBuilder(openAiChatModel, ObservationRegistry.NOOP, null).build();
    this.environment = environment;
    this.restTemplate = new RestTemplate();
  }

  @PostMapping("/api/connectors/wiki/generate-embeddings")
  public void callGenerateEmbeddings() {
    generateEmbeddings();
  }

  @Scheduled(cron = "0 0/10 * * * ?")
  public void performEmbedding() {
    log.info("Scheduled task executed at: {}", System.currentTimeMillis());
    generateEmbeddings();
  }

  private void generateEmbeddings() {
    List<Map<String, Object>> pagesUpdatedWithinLast10Minutes =
        pageRepository.findPagesUpdatedWithinLast10Minutes();
    log.info(
        "Found {} pages updated within last 10 minutes", pagesUpdatedWithinLast10Minutes.size());
    for (Map<String, Object> page : pagesUpdatedWithinLast10Minutes) {
      List<String> content = markdownParser.extractSections((String) page.get("content"));

      if (content.isEmpty()) {
        log.info("No content found for page {}", page.get("id"));
        continue;
      }

      pageEmbeddingRepository.deleteByPageId((Integer) page.get("id"));
      log.info("Deleted embeddings for page {}", page.get("id"));
      for (String section : content) {
        PageEmbedding pageEmbedding = new PageEmbedding();
        pageEmbedding.setContent(section);
        pageEmbedding.setPage(pageRepository.findById((Integer) page.get("id")).get());
        pageEmbeddingRepository.saveAndFlush(pageEmbedding);
        log.info("Saved embeddings for page {}", page.get("id"));

        try {
          float[] embed = openAiEmbeddingModel.embed(section);
          pageEmbeddingRepository.updateLocalEmbeddings(pageEmbedding.getId(), embed);
          log.info("Updated local embeddings for page {}", page.get("id"));
        } catch (org.springframework.web.client.ResourceAccessException
            | NonTransientAiException e) {
          log.error("Failed to update local embeddings for page {}", page.get("id"), e);
        }

        if (isOnlineAiEnabled()) {
          try {
            float[] embed = mistralAiEmbeddingModel.embed(section);
            pageEmbeddingRepository.updateMistralEmbeddings(pageEmbedding.getId(), embed);
            log.info("Updated mistral embeddings for page {}", page.get("id"));
          } catch (org.springframework.web.client.ResourceAccessException e) {
            log.error("Failed to update mistral embeddings for page {}", page.get("id"), e);
          }
        }
      }
    }
  }

  private boolean isOnlineAiEnabled() {
    return "true".equalsIgnoreCase(environment.getProperty("online.enabled"));
  }

  @PostMapping("/api/connectors/wiki/ask")
  public org.springframework.http.ResponseEntity<Map<String, String>> getAnswer(
      @RequestBody Map<String, String> payload) {
    String question = payload.get("message");
    String model = payload.get("model");
    float[] embed = getEmbeddingByModel(model, question);
    if (embed == null) {
      return ResponseEntity.badRequest().body(Map.of("message", "Invalid model"));
    }

    // Perform similarity search based on the model type
    List<Map<String, Object>> maps = performSimilaritySearch(model, embed);
    if (CollectionUtils.isEmpty(maps)) {
      log.info("No pages found for question {}", question);
      return ResponseEntity.ok(
          Map.of("message", "I don't know the answer, no supporting pages found"));
    }
    log.info("Found pages for question {}", question);
    if (model.equalsIgnoreCase("local")) {
      ChatClient.CallResponseSpec response =
          openaiChatClient.prompt().system(generateSystemPrompt(maps)).user(question).call();
      return ResponseEntity.ok(Map.of("message", Objects.requireNonNull(response.content()), "reference", "" + maps.getFirst().get("path")));
    } else if (model.equalsIgnoreCase("online")) {
      ChatClient.CallResponseSpec response =
          mistralChatClient.prompt().system(generateSystemPrompt(maps)).user(question).call();
      return ResponseEntity.ok(Map.of("message", Objects.requireNonNull(response.content()), "reference", "" + maps.getFirst().get("path")));
    } else {
      return ResponseEntity.badRequest().body(Map.of("message", "Invalid model"));
    }
  }

  private float[] getEmbeddingByModel(String model, String question) {
    if (model.equalsIgnoreCase("local")) {
      log.info("Searching for pages using local model");
      return openAiEmbeddingModel.embed(question);
    } else if (model.equalsIgnoreCase("online")) {
      log.info("Searching for pages using mistral model");
      return mistralAiEmbeddingModel.embed(question);
    } else {
      log.error("Invalid model {}", model);
      return null;
    }
  }

  private List<Map<String, Object>> performSimilaritySearch(String model, float[] embed) {
    if ("local".equalsIgnoreCase(model)) {
      List<Map<String, Object>> maps = pageEmbeddingRepository.localSimilaritySearch(embed, 50, 1);
      if (!CollectionUtils.isEmpty(maps)) {
        log.info("Found {} pages using local model", maps.getFirst().get("score"));
      }
      return maps;
    } else if ("online".equalsIgnoreCase(model)) {
      return pageEmbeddingRepository.mistralSimilaritySearch(embed, 50, 1);
    } else {
      return List.of();
    }
  }

  private String generateSystemPrompt(List<Map<String, Object>> documents) {
    StringBuilder prompt =
        new StringBuilder(
            """
You are a helpful assistant to answer user question with access to relevant information from office wiki.
Instructions for responses:
1. Analyze the provided document excerpts carefully
2. Provide accurate, concise answers focused on document content
3. Format responses in HTML using appropriate tags (<p>, <ul>, <b> etc.)
4. Only make statements that are directly supported by the documents
5. Answer should not be more than one paragraph
6. If information is incomplete or unclear, respond with exactly: "I don\\'t know the answer, please ask again with more details"

The relevant document excerpt follows:
""");
    documents.forEach(map -> prompt.append(map.get("content")));
    return prompt.toString();
  }

  @GetMapping("/api/connectors/env")
  public AIServiceStatus getEnv() {
    String openAiKey =
        "XXXXX"
            + Objects.requireNonNull(environment
                        .getProperty("spring.ai.openai.api-key"))
                .substring(
                    Objects.requireNonNull(environment.getProperty("spring.ai.openai.api-key"))
                            .length()
                        - 5);
    String mistralKey =
        "XXXXX"
            + Objects.requireNonNull(environment
                        .getProperty("spring.ai.mistralai.api-key"))
                .substring(
                    Objects.requireNonNull(environment.getProperty("spring.ai.mistralai.api-key"))
                            .length()
                        - 5);
    String healthEndpoint =
        isOnlineAiEnabled()
            ? "https://api.mistral.ai"
            : environment.getProperty("spring.ai.openai.base-url");
    String aiHealth = "false";
    try {
      // Make a GET request to the health endpoint
      assert healthEndpoint != null;
      ResponseEntity<String> response = restTemplate.getForEntity(healthEndpoint, String.class);
      float[] embedResponse = openAiEmbeddingModel.embed("health check");
      if (response.getStatusCode().is2xxSuccessful() && embedResponse.length > 0) {
        aiHealth = "true";
      } else {
        log.error("AI Health check failed with code: {}", response.getStatusCode());
      }
    } catch (Exception e) {
      // Handle any exceptions, e.g. if the server is down or not reachable
      log.error("AI Health check failed: {}", e.getMessage());
    }
    return new AIServiceStatus(
        LocalTime.now().toString(),
        aiHealth,
        isOnlineAiEnabled() ? "online" : "local",
        healthEndpoint,
        isOnlineAiEnabled() ? mistralKey : openAiKey,
        environment.getProperty("wiki.url"),
        environment.getProperty("wiki.name")
    );
  }

  public record AIServiceStatus(
      String timestamp, String status, String type, String url, String key, String wiki, String name) {}
}
