package com.serkowski.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
public class ChatCompletionService {

    private final ChatClient chatClient;

    public ChatCompletionService(ChatClient chatClient) {
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
}

