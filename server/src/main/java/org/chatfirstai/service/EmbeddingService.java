package org.chatfirstai.service;

import java.util.List;
import java.util.Map;
import org.chatfirstai.data.PageEmbedding;
import org.chatfirstai.data.PageEmbeddingRepository;
import org.chatfirstai.data.PageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mistralai.MistralAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class EmbeddingService {

  private static final Logger log = LoggerFactory.getLogger(EmbeddingService.class);

  private final PageRepository pageRepository;
  private final PageEmbeddingRepository pageEmbeddingRepository;
  private final OpenAiEmbeddingModel openAiEmbeddingModel;
  private final MistralAiEmbeddingModel mistralAiEmbeddingModel;
  private final MarkdownParserService markdownParserService;
  private final Environment environment;

  public EmbeddingService(
      PageRepository pageRepository,
      PageEmbeddingRepository pageEmbeddingRepository,
      OpenAiEmbeddingModel openAiEmbeddingModel,
      MistralAiEmbeddingModel mistralAiEmbeddingModel,
      MarkdownParserService markdownParserService,
      Environment environment) {
    this.pageRepository = pageRepository;
    this.pageEmbeddingRepository = pageEmbeddingRepository;
    this.openAiEmbeddingModel = openAiEmbeddingModel;
    this.mistralAiEmbeddingModel = mistralAiEmbeddingModel;
    this.markdownParserService = markdownParserService;
    this.environment = environment;
  }

  public void generateEmbeddings() {
    List<Map<String, Object>> pages = pageRepository.findPagesUpdatedWithinLast10Minutes();
    log.info("Found {} pages updated within last 10 minutes", pages.size());
    pages.forEach(this::processPageEmbedding);
  }

  private void processPageEmbedding(Map<String, Object> page) {
    Integer pageId = (Integer) page.get("id");
    String content = (String) page.get("content");
    List<String> sections =
        markdownParserService.extractSections(content); // Assuming static util method
    if (sections.isEmpty()) {
      log.info("No content found for page {}", pageId);
      return;
    }

    pageEmbeddingRepository.deleteByPageId(pageId);
    log.info("Deleted embeddings for page {}", pageId);

    sections.forEach(
        section -> {
          PageEmbedding embedding = new PageEmbedding();
          embedding.setContent(section);
          embedding.setPage(pageRepository.findById(pageId).orElseThrow());
          pageEmbeddingRepository.saveAndFlush(embedding);
          log.info("Saved embeddings for page {}", pageId);

          try {
            float[] embed = openAiEmbeddingModel.embed(section);
            pageEmbeddingRepository.updateLocalEmbeddings(embedding.getId(), embed);
            log.info("Updated local embeddings for page {}", pageId);
          } catch (org.springframework.web.client.ResourceAccessException
              | NonTransientAiException e) {
            log.error("Failed to update local embeddings for page {}", pageId, e);
          }
        });
  }

  public float[] getEmbeddingByModel(String model, String text) {
    if (isOnlineAiEnabled()) {
      log.info("Using mistral model for embedding");
      return mistralAiEmbeddingModel.embed(text);
    } else {
      log.info("Using local model for embedding");
      return openAiEmbeddingModel.embed(text);
    }
  }

  private boolean isOnlineAiEnabled() {
    return "true".equalsIgnoreCase(environment.getProperty("online.enabled"));
  }

  public List<Map<String, Object>> performSimilaritySearch(String model, float[] embed) {
    if ("local".equalsIgnoreCase(model)) {
      List<Map<String, Object>> results =
          pageEmbeddingRepository.localSimilaritySearch(embed, 70, 1);
      if (!CollectionUtils.isEmpty(results)) {
        log.info(
            "Similarity search (local) returned a top score of {}", results.get(0).get("score"));
      }
      return results;
    } else if ("online".equalsIgnoreCase(model)) {
      return pageEmbeddingRepository.mistralSimilaritySearch(embed, 70, 2);
    }
    return List.of();
  }
}
