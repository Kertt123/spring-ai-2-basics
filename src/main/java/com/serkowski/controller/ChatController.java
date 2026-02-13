package com.serkowski.controller;

import com.serkowski.model.text.TextRequest;
import com.serkowski.services.ChatCompletionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatCompletionService chatService;

    public ChatController(ChatCompletionService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/text")
    Mono<String> text(@RequestBody Mono<TextRequest> requestBody) {
        return requestBody.flatMap(request -> chatService.getCompletions(request.message(), request.conversationId()));
    }
}
