package com.serkowski.services;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.messages.Message;

import java.util.ArrayList;
import java.util.List;

public class PersonalAdvisor implements BaseAdvisor {

    @Override
    public String getName() {
        return "personalAdvisor";
    }

    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
        List<Message> instructions = chatClientRequest.prompt().getInstructions();
        Message lastUserMessage = instructions.getLast();
        System.out.println("[PersonalAdvisor] Last user message: " + lastUserMessage.getText());
        return chatClientRequest;
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        List<String> assistantMessages = new ArrayList<>();
        if (chatClientResponse.chatResponse() != null) {
            assistantMessages = chatClientResponse.chatResponse()
                    .getResults()
                    .stream()
                    .map(g -> (Message) g.getOutput())
                    .map(Message::getText)
                    .toList();
        }
        System.out.println("[PersonalAdvisor] Last ai message: " + String.join("", assistantMessages));
        return chatClientResponse;
    }

    @Override
    public int getOrder() {
        return DEFAULT_CHAT_MEMORY_PRECEDENCE_ORDER + 1;
    }
}
