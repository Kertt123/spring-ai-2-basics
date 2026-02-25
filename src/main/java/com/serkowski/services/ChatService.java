package com.serkowski.services;

import com.serkowski.model.text.MovieRecommendationResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
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

    public String getCompletionsWithImageUrl(String message, String imgType, String imageUrl, String conversationId) {
        try {
            UrlResource resource = new UrlResource(imageUrl);
            return chatClient.prompt()
                    .user(userSpec -> userSpec
                            .text(message)
                            .media(MimeType.valueOf(imgType), resource))
                    .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                    .call()
                    .content();
        } catch (Exception e) {
            throw new IllegalArgumentException("Not correct image URL " + imageUrl, e);
        }
    }

    public String getCompletionsWithImagePath(String message, String imgType, String imgPath, String conversationId) {
        try {
            ClassPathResource resource = new ClassPathResource(imgPath);
            return chatClient.prompt()
                    .user(userSpec -> userSpec
                            .text(message)
                            .media(MimeType.valueOf(imgType), resource))
                    .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                    .call()
                    .content();
        } catch (Exception e) {
            throw new IllegalArgumentException("Not correct image path " + imgPath, e);
        }
    }

    public String getCompletionsWithImage(String message, String imgType, byte[] image, String conversationId) {
        return chatClient.prompt()
                .user(userSpec -> userSpec
                        .text(message)
                        .media(MimeType.valueOf(imgType), new ByteArrayResource(image)))
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
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
