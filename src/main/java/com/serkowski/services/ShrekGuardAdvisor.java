package com.serkowski.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.messages.AssistantMessage;

public class ShrekGuardAdvisor implements BaseAdvisor {
    private static final Logger logger = LoggerFactory.getLogger(ShrekGuardAdvisor.class);

    @Override
    public String getName() {
        return "shrek-guard-advisor";
    }

    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
        return chatClientRequest;
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        var chatResponse = chatClientResponse.chatResponse();
        if (chatResponse == null || chatResponse.getResult() == null) {
            return chatClientResponse;
        }
        String originalText = chatResponse.getResult().getOutput().getText();
        if (originalText == null) {
            return chatClientResponse;
        }
        String maskedText = originalText.replaceAll("(?i)Shrek|Fiona|Donkey", "[HIDDEN]");
        if (!originalText.equals(maskedText)) {
            logger.info("[ShrekGuardAdvisor] masked AI response: {}", maskedText);
            var maskedOutput = AssistantMessage.builder()
                    .content(maskedText)
                    .properties(chatResponse.getResult().getOutput().getMetadata())
                    .build();
            var maskedGeneration = new org.springframework.ai.chat.model.Generation(maskedOutput, chatResponse.getResult().getMetadata());
            var maskedChatResponse = new org.springframework.ai.chat.model.ChatResponse(java.util.List.of(maskedGeneration), chatResponse.getMetadata());
            return chatClientResponse.mutate().chatResponse(maskedChatResponse).build();
        }
        return chatClientResponse;
    }

    @Override
    public int getOrder() {
        return DEFAULT_CHAT_MEMORY_PRECEDENCE_ORDER + 1;
    }
}
