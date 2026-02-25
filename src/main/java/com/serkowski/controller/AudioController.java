package com.serkowski.controller;

import com.serkowski.model.text.TextRequest;
import com.serkowski.services.AudioService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/audio")
public class AudioController {

    private final AudioService audioService;

    public AudioController(AudioService audioService) {
        this.audioService = audioService;
    }

    @PostMapping(value = "/transcribe", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String transcribe(@RequestPart("file") MultipartFile file) {
        return audioService.transcribeAudio(file.getResource());
    }


    @PostMapping(value = "/generateAudio", produces = "audio/mpeg")
    byte[] generateAudio(@RequestBody TextRequest request) {
        return audioService.generateAudio(request.message());
    }

}
