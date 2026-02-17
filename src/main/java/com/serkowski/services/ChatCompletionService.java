package com.serkowski.services;

import com.serkowski.model.text.MovieRecommendationResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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

    public Mono<MovieRecommendationResponse> movieRecommendation(String message, String conversationId) {
        return Mono.fromCallable(() -> chatClient.prompt()
                        .system("You are a movie recommendation assistant. Based on the user's preferences, recommend 5 movies.")
                        .user(message)
                        .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                        .call()
                        .entity(MovieRecommendationResponse.class))
                .subscribeOn(Schedulers.boundedElastic());
    }
}

