package org.chatfirstai.service;

import java.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HealthCheckService {
  private static final Logger log = LoggerFactory.getLogger(HealthCheckService.class);
  private final Environment environment;
  private final EmbeddingService embeddingService;
  private final RestTemplate restTemplate;

  public HealthCheckService(Environment environment, EmbeddingService embeddingService) {
    this.environment = environment;
      this.embeddingService = embeddingService;
      this.restTemplate = new RestTemplate();
  }

  private String maskKey(String key) {
    if (key == null || key.length() < 5) {
      return "XXXXX";
    }
    return "XXXXX" + key.substring(key.length() - 5);
  }

  public boolean isOnlineAiEnabled() {
    return "true".equalsIgnoreCase(environment.getProperty("online.enabled"));
  }

  public AIServiceStatus healthCheck() {
    String openAiKey = maskKey(environment.getProperty("spring.ai.openai.api-key"));
    String mistralKey =maskKey(environment.getProperty("spring.ai.mistralai.api-key"));
    String healthEndpoint =
        isOnlineAiEnabled()
            ? "https://api.mistral.ai"
            : environment.getProperty("spring.ai.openai.base-url");
    String aiHealth = "false";
    try {
      // Make a GET request to the health endpoint
      assert healthEndpoint != null;
      ResponseEntity<String> response = restTemplate.getForEntity(healthEndpoint, String.class);
      float[] embedResponse = embeddingService.getEmbeddingByModel(isOnlineAiEnabled()? "online" : "local", "health check");
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
        environment.getProperty("wiki.name"));
  }

  public record AIServiceStatus(
      String timestamp,
      String status,
      String type,
      String url,
      String key,
      String wiki,
      String name) {}
}
