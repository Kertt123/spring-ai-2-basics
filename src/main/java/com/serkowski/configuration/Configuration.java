package com.serkowski.configuration;

import com.serkowski.services.AudioService;
import com.serkowski.services.ChatCompletionService;
import com.serkowski.services.ImageService;
import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public JdbcChatMemoryRepository chatMemoryRepository(JdbcTemplate jdbcTemplate) {
        return JdbcChatMemoryRepository.builder()
                .jdbcTemplate(jdbcTemplate)
                .build();
    }

    @Bean
    public ChatClient chatClient(ChatModel chatModel, JdbcChatMemoryRepository chatMemoryRepository) {
        return ChatClient.builder(chatModel)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(MessageWindowChatMemory.builder()
                                        .chatMemoryRepository(chatMemoryRepository)
                                        .maxMessages(30)
                                        .build())
                                .build(),
                        new SimpleLoggerAdvisor()
                )
                .build();
    }


    @Bean
    public ChatCompletionService chatService(ChatClient chatClient) {
        return new ChatCompletionService(chatClient);
    }

    @Bean
    public AudioService audioService(TranscriptionModel transcriptionModel, OpenAiAudioSpeechModel openAiAudioSpeechModel) {
        return new AudioService(transcriptionModel, openAiAudioSpeechModel);
    }

    @Bean
    public ImageService imageService(ChatClient chatClient, OpenAiImageModel openAiImageModel) {
        return new ImageService(chatClient, openAiImageModel);
    }

    @Bean
    public WebClient webClient() {
        int bufferSize = 16 * 1024 * 1024;
        return WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(bufferSize))
                        .build())
                .build();
    }


}
