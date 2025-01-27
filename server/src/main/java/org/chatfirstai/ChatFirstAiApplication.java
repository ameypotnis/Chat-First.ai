package org.chatfirstai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ChatFirstAiApplication {

  public static void main(String[] args) {
    SpringApplication.run(ChatFirstAiApplication.class, args);
  }
}
