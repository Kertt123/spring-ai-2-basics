package com.serkowski.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

@Service
public class ImageService {

    private final ChatClient chatClient;

    public ImageService(ChatClient chatClient) {
        this.chatClient = chatClient;
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
}

