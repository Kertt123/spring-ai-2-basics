package com.serkowski.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Base64;
import java.util.stream.Collectors;

@Service
public class ImageService {

    private final ChatClient chatClient;
    private final OpenAiImageModel openAiImageModel;

    public ImageService(ChatClient chatClient, OpenAiImageModel openAiImageModel) {
        this.chatClient = chatClient;
        this.openAiImageModel = openAiImageModel;
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

    public Mono<String> getCompletionsWithImagePath(String message, String imgType, String imgPath, String conversationId) {
        return Mono.defer(() -> {
            try {
                ClassPathResource resource = new ClassPathResource(imgPath);
                return chatClient.prompt()
                        .user(userSpec -> userSpec
                                .text(message)
                                .media(MimeType.valueOf(imgType), resource))
                        .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                        .stream()
                        .content()
                        .collect(Collectors.joining());
            } catch (Exception e) {
                return Mono.error(new IllegalArgumentException("Not correct image path " + imgPath, e));
            }
        });
    }

    public Mono<String> getCompletionsWithImage(String message, String imgType, byte[] image, String conversationId) {
        return chatClient.prompt()
                .user(userSpec -> userSpec
                        .text(message)
                        .media(MimeType.valueOf(imgType), new ByteArrayResource(image)))
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .stream()
                .content()
                .collect(Collectors.joining());
    }

    public Mono<byte[]> generateImage(String prompt, String quality, int height, int width) {
        return Mono.defer(() -> {
                    try {
                        ImageResponse response = openAiImageModel.call(
                                new ImagePrompt(prompt,
                                        OpenAiImageOptions.builder()
                                                .quality(quality)
                                                .N(1)
                                                .height(height)
                                                .responseFormat("b64_json")
                                                .width(width).build())

                        );
                        String b64Json = response.getResult().getOutput().getB64Json();
                        byte[] decodedBytes = Base64.getDecoder().decode(b64Json);
                        return Mono.just(decodedBytes);
                    } catch (Exception e) {
                        return Mono.error(new IllegalArgumentException("Error during image generation", e));
                    }
                })
                .subscribeOn(Schedulers.boundedElastic());
    }
}

