package org.chatfirstai;

import java.util.List;
import java.util.Map;
import org.chatfirstai.service.ChatService;
import org.chatfirstai.service.EmbeddingService;
import org.chatfirstai.service.HealthCheckService;
import org.chatfirstai.service.PromptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/connectors")
public class ConnectorController {

  private static final Logger log = LoggerFactory.getLogger(ConnectorController.class);

  private final EmbeddingService embeddingService;
  private final ChatService chatService;
  private final PromptService promptService;
  private final HealthCheckService healthCheckService;

  public ConnectorController(
      EmbeddingService embeddingService,
      ChatService chatService,
      PromptService promptService,
      HealthCheckService healthCheckService) {
    this.embeddingService = embeddingService;
    this.chatService = chatService;
    this.promptService = promptService;
    this.healthCheckService = healthCheckService;
  }

  @PostMapping("/wiki/generate-embeddings")
  public ResponseEntity<String> generateEmbeddings() {
    try {
      embeddingService.generateEmbeddings();
      return ResponseEntity.ok("Embeddings generated successfully");
    } catch (Exception e) {
      log.error("Error generating embeddings", e);
      return ResponseEntity.internalServerError().body("Error generating embeddings");
    }
  }

  @PostMapping("/wiki/ask")
  public ResponseEntity<Map<String, String>> getAnswer(
      @RequestBody AskDto payload) {
    float[] embed = embeddingService.getEmbeddingByModel(payload.model, payload.message);
    if (embed == null) {
      return ResponseEntity.badRequest().body(Map.of("message", "Invalid model"));
    }

    List<Map<String, Object>> results = embeddingService.performSimilaritySearch(payload.model, embed);
    if (CollectionUtils.isEmpty(results)) {
      log.info("No pages found for question {}", payload.message);
      return ResponseEntity.ok(Map.of("message", "I don't know the answer, no supporting pages found"));
    }

    String systemPrompt = promptService.generateSystemPrompt(results);
    String answer = chatService.getAnswer(payload.model, payload.message, systemPrompt);
    return ResponseEntity.ok(Map.of("message", answer));
  }

  @GetMapping("/env")
  public HealthCheckService.AIServiceStatus getEnv() {
    return healthCheckService.healthCheck();
  }

  public record AskDto(String message, String model) {}
}
