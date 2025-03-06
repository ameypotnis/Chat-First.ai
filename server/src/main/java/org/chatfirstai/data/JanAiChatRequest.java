package org.chatfirstai.data;

import java.util.List;

public record JanAiChatRequest(List<Message> messages, String model) {
  public record Message(String role, String content) {}
}
