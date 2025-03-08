package org.chatfirstai.service;

import java.util.List;
import java.util.Map;
import org.chatfirstai.data.JanAiChatRequest;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JanAiService {

  private final RestTemplate restTemplate;
  private final Environment environment;

  public JanAiService(RestTemplate restTemplate, Environment environment) {
    this.restTemplate = restTemplate;
    this.environment = environment;
  }

  public String getResponse(List<JanAiChatRequest.Message> messages) {
    JanAiChatRequest request =
        new JanAiChatRequest(messages, environment.getProperty("jan-ai.model"));
    String apiUrl =
        String.format("%s%s", environment.getProperty("jan-ai.base-url"), "/v1/chat/completions");
    Map jsonMap = restTemplate.postForObject(apiUrl, request, Map.class);
    Map message = ((Map) ((Map) ((List) jsonMap.get("choices")).getFirst()).get("message"));
    return (String) message.get("content");
  }
}
