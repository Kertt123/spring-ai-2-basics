package com.serkowski.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.messages.Message;

public class ShrekGuardAdvisor implements BaseAdvisor {
    private static final Logger logger = LoggerFactory.getLogger(ShrekGuardAdvisor.class);

    @Override
    public String getName() {
        return "shrek-guard-advisor";
    }

    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
        Message lastUserMessage = chatClientRequest.prompt().getUserMessage();
        String originalText = lastUserMessage.getText();
        String maskedText = originalText.replaceAll("(?i)Shrek|Fiona|Donkey", "[HIDDEN]");
        if (!originalText.equals(maskedText)) {
            logger.info("[ShrekGuardAdvisor] masked data user message: {}", maskedText);
            return chatClientRequest.mutate()
                    .prompt(chatClientRequest.prompt().augmentUserMessage(maskedText))
                    .build();
        }
        return chatClientRequest;
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        return chatClientResponse;
    }

    @Override
    public int getOrder() {
        return DEFAULT_CHAT_MEMORY_PRECEDENCE_ORDER + 1;
    }
}
