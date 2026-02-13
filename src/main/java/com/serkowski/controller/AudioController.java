package com.serkowski.controller;

import com.serkowski.model.text.TextRequest;
import com.serkowski.services.AudioService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/audio")
public class AudioController {

    private final AudioService audioService;

    public AudioController(AudioService audioService) {
        this.audioService = audioService;
    }

    @PostMapping(value = "/transcribe", consumes = "audio/mp4")
    Mono<String> transcribe(@RequestBody byte[] audioBytes) {
        return audioService.transcribeAudio(audioBytes);
    }

    @PostMapping(value = "/generateAudio", produces = "audio/mpeg")
    Flux<byte[]> generateAudio(@RequestBody TextRequest request) {
        return audioService.generateAudio(request.message());
    }

}
