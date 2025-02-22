package org.chatfirstai.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PromptService {

  public String generateSystemPrompt(List<Map<String, Object>> documents) {
    StringBuilder prompt =
        new StringBuilder(
            """
                You are a helpful assistant to answer user questions using relevant information from the office wiki.
                Instructions for responses:
                1. Analyze the provided document excerpts carefully.
                2. Provide accurate, concise answers focused on document content.
                3. Format responses in HTML using appropriate tags (<p>, <ul>, etc.).
                4. Only make statements that are directly supported by the documents.
                5. If information is incomplete or unclear, respond with exactly: "I don\\'t know the answer"

                The relevant document excerpt follows:
                """);
    documents.forEach(doc -> prompt.append(doc.get("content")));
    return prompt.toString();
  }
}
