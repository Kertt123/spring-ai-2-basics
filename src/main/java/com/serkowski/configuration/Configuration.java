package com.serkowski.configuration;

import com.serkowski.services.ChatService;
import com.serkowski.services.PersonalAdvisor;
import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(MessageWindowChatMemory.builder()
                                        .maxMessages(30)
                                        .build())
                                .build(),
                        new SimpleLoggerAdvisor(),
                        new PersonalAdvisor()
                )
                .build();
    }


    @Bean
    public ChatService chatService(ChatClient chatClient, OpenAiImageModel openAiImageModel, TranscriptionModel transcriptionModel, OpenAiAudioSpeechModel openAiAudioSpeechModel) {
        return new ChatService(chatClient, openAiImageModel, transcriptionModel, openAiAudioSpeechModel);
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
