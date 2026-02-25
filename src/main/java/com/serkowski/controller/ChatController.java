package com.serkowski.controller;

import com.serkowski.model.text.MovieRecommendationResponse;
import com.serkowski.model.text.TextRequest;
import com.serkowski.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/chat")
public class ChatController {


    @Autowired
    private ChatService chatService;

    @PostMapping("/text")
    String text(@RequestBody TextRequest request) {
        return chatService.getCompletions(request.message(), request.conversationId());
    }

    @PostMapping(value = "/textStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<String> textStream(@RequestBody TextRequest request) {
        return chatService.getCompletionsStream(request.message(), request.conversationId());
    }

    @PostMapping("/movieRecommendation")
    MovieRecommendationResponse movieRecommendation(@RequestBody TextRequest request) {
        return chatService.movieRecommendation(request.message(), request.conversationId());
    }
}
