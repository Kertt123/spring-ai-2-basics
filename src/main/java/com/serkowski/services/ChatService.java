package com.serkowski.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
public class ChatService {

    ChatClient chatClient;

    public ChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public Mono<String> getCompletions(String message, String conversationId) {
        return chatClient.prompt()
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .stream()
                .content()
                .collect(Collectors.joining());
    }

    public Mono<String> getCompletionsWithImageUrl(String message, String imgType, String imageUrl, String conversationId) {
        return Mono.defer(() -> {
            try {
                UrlResource resource = new UrlResource(imageUrl);
                return chatClient.prompt()
                        .user(userSpec -> userSpec
                                .text(message)
                                .media(MimeType.valueOf(imgType), resource))
                        .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                        .stream()
                        .content()
                        .collect(Collectors.joining());
            } catch (Exception e) {
                return Mono.error(new IllegalArgumentException("Not correct image URL " + imageUrl, e));
            }
        });
    }

    public Mono<String> getCompletionsWithImagePath(String message, String imgType, String imgPath) {
        return Mono.defer(() -> {
            try {
                ClassPathResource resource = new ClassPathResource(imgPath);
                return chatClient.prompt()
                        .user(userSpec -> userSpec
                                .text(message)
                                .media(MimeType.valueOf(imgType), resource))
                        .stream()
                        .content()
                        .collect(Collectors.joining());
            } catch (Exception e) {
                return Mono.error(new IllegalArgumentException("Not correct image path " + imgPath, e));
            }
        });
    }

    public Mono<String> getCompletionsWithImage(String message, String imgType, byte[] image) {
        return chatClient.prompt()
                .user(userSpec -> userSpec
                        .text(message)
                        .media(MimeType.valueOf(imgType), new ByteArrayResource(image)))
                .stream()
                .content()
                .collect(Collectors.joining());
    }
}
