package com.serkowski.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OpenAIChatService {
    ChatClient chatClient;
    OpenAiImageModel openAiImageModel;

    public OpenAIChatService(ChatClient chatClient, OpenAiImageModel openAiImageModel) {
        this.chatClient = chatClient;
        this.openAiImageModel = openAiImageModel;
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
                                        .width(width).build())

                );
                return Mono.just(response.getResult().getOutput().getB64Json().getBytes());
            } catch (Exception e) {
                return Mono.error(new IllegalArgumentException("Error during image generation", e));
            }
        });
    }
}
