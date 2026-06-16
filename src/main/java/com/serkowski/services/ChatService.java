package com.serkowski.services;

import com.serkowski.model.text.MovieRecommendationResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatService {

    ChatClient chatClient;

    public ChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String chat(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    public String chatWithMemory(String message, String conversationId) {
        return chatClient.prompt()
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }

    public Flux<String> chatStream(String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }

    public MovieRecommendationResponse movieRecommendation(String message) {
        return chatClient.prompt()
                .system("You are a movie recommendation assistant. Based on the user's preferences, recommend 5 movies.")
                .user(message)
                .call()
                .entity(MovieRecommendationResponse.class);
    }
}

