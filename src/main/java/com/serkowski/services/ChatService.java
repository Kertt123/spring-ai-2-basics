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

    public String getCompletions(String message, String conversationId) {
        return chatClient.prompt()
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }

    public Flux<String> getCompletionsStream(String message, String conversationId) {
        return chatClient.prompt()
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .stream()
                .content();
    }

    public MovieRecommendationResponse movieRecommendation(String message, String conversationId) {
        return chatClient.prompt()
                .system("You are a movie recommendation assistant. Based on the user's preferences, recommend 5 movies.")
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .entity(MovieRecommendationResponse.class);
    }
}
