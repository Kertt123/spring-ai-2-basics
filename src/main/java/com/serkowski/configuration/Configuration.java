package com.serkowski.configuration;

import com.serkowski.services.ChatService;
import com.serkowski.services.DialBucketClient;
import com.serkowski.services.DialWebService;
import com.serkowski.services.PersonalAdvisor;
//import org.springframework.ai.azure.openai.AzureOpenAiChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@org.springframework.context.annotation.Configuration
public class Configuration {

//    @Bean
//    public ChatClient chatClient(ChatModel chatModel) {
//        return ChatClient.builder(chatModel)
//                .defaultAdvisors(
//                        MessageChatMemoryAdvisor.builder(MessageWindowChatMemory.builder()
//                                        .maxMessages(30)
//                                        .build())
//                                .build(),
//                        new SimpleLoggerAdvisor(),
//                        new PersonalAdvisor()
//                )
//                .defaultOptions(AzureOpenAiChatOptions.builder()
//                        .deploymentName("gpt-4o")
//                        .build())
//                .build();
//    }

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
    public ChatService chatService(ChatClient chatClient) {
        return new ChatService(chatClient);
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

//    @Bean
//    public DialBucketClient dialBucketClient(WebClient webClient, @Value("${spring.ai.azure.openai.endpoint}") String url, @Value("${spring.ai.azure.openai.api-key}") String apiKey) {
//        return new DialBucketClient(webClient, url, apiKey);
//    }
//
//    @Bean
//    public DialWebService dialWebService(WebClient webClient, @Value("${spring.ai.azure.openai.endpoint}") String url, @Value("${spring.ai.azure.openai.api-key}") String apiKey, DialBucketClient dialBucketClient) {
//        return new DialWebService(webClient, url, apiKey, dialBucketClient);
//    }


    @Bean
    public DialBucketClient dialBucketClient(WebClient webClient, @Value("dummy") String url, @Value("${spring.ai.openai.api-key}") String apiKey) {
        return new DialBucketClient(webClient, url, apiKey);
    }

    @Bean
    public DialWebService dialWebService(WebClient webClient, @Value("dummy") String url, @Value("${spring.ai.openai.api-key}") String apiKey, DialBucketClient dialBucketClient) {
        return new DialWebService(webClient, url, apiKey, dialBucketClient);
    }


}
