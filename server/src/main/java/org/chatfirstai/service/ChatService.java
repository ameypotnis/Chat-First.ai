package org.chatfirstai.service;

import io.micrometer.observation.ObservationRegistry;
import java.util.List;
import org.chatfirstai.data.JanAiChatRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.DefaultChatClientBuilder;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

  private final ChatClient mistralChatClient;
  private final ChatClient openaiChatClient;
  private final JanAiService janAiService;
  private final HealthCheckService healthCheckService;

  public ChatService(
      @Qualifier("mistralAiChatModel") ChatModel mistralAiChatModel,
      @Qualifier("openAiChatModel") ChatModel openAiChatModel,
      JanAiService janAiService,
      HealthCheckService healthCheckService) {
    this.mistralChatClient =
        new DefaultChatClientBuilder(mistralAiChatModel, ObservationRegistry.NOOP, null).build();
    this.openaiChatClient =
        new DefaultChatClientBuilder(openAiChatModel, ObservationRegistry.NOOP, null).build();
    this.janAiService = janAiService;
    this.healthCheckService = healthCheckService;
  }

  public String getAnswer(String model, String question, String systemPrompt) {
    if (healthCheckService.isJanAiEnabled()) {
      return janAiService.getResponse(
          List.of(
              new JanAiChatRequest.Message("system", systemPrompt),
              new JanAiChatRequest.Message("user", question)));
    }
    if (healthCheckService.isOnlineAiEnabled()) {
      return mistralChatClient.prompt().system(systemPrompt).user(question).call().content();
    } else {
      return openaiChatClient.prompt().system(systemPrompt).user(question).call().content();
    }
  }
}
