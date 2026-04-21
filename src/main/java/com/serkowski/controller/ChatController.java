package com.serkowski.controller;

import com.serkowski.model.text.MovieRecommendationResponse;
import com.serkowski.model.text.TextRequest;
import com.serkowski.model.text.TextRequestSimple;
import com.serkowski.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
public class ChatController {


    @Autowired
    private ChatService chatService;

    @PostMapping("/text")
    String text(@RequestBody TextRequestSimple request) {
        return chatService.chat(request.message());
    }

    @PostMapping("/textWithMemory")
    String textWithMemory(@RequestBody TextRequest request) {
        return chatService.chatWithMemory(request.message(), request.conversationId());
    }

    @PostMapping(value = "/textStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<String> textStream(@RequestBody TextRequestSimple request) {
        return chatService.chatStream(request.message());
    }

    @PostMapping("/movieRecommendation")
    MovieRecommendationResponse movieRecommendation(@RequestBody TextRequestSimple request) {
        return chatService.movieRecommendation(request.message());
    }
}
