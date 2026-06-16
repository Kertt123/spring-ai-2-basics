package com.serkowski.configuration;

import com.anthropic.models.messages.Model;
import com.serkowski.services.ChatService;
import com.serkowski.services.ShrekGuardAdvisor;
import org.springframework.ai.anthropic.AnthropicChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

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
//                        new SafeGuardAdvisor(List.of("Shrek", "Fiona", "Donkey")),
                        new SimpleLoggerAdvisor(),
                        new ShrekGuardAdvisor()
                )
                .defaultOptions(AnthropicChatOptions.builder()
                        .model(Model.CLAUDE_SONNET_4_6))
                .build();
    }


    @Bean
    public ChatService chatService(ChatClient chatClient) {
        return new ChatService(chatClient);
    }

    @Bean
    public RestClient restClient() {
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(
                HttpClient.newBuilder()
                        .connectTimeout(Duration.ofMinutes(2))
                        .build()
        );
        requestFactory.setReadTimeout(Duration.ofMinutes(2));
        return RestClient.builder()
                .requestFactory(requestFactory)
                .build();
    }

}
