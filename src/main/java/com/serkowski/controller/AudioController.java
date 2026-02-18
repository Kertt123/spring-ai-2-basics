package com.serkowski.controller;

import com.serkowski.model.text.TextRequest;
import com.serkowski.services.AudioService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/audio")
public class AudioController {

    private final AudioService audioService;

    public AudioController(AudioService audioService) {
        this.audioService = audioService;
    }

    @PostMapping(value = "/transcribe", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Mono<String> transcribe(@RequestPart("file") Mono<FilePart> filePartMono) {
        return filePartMono.flatMap(filePart ->
                Mono.using(
                        () -> Files.createTempFile("upload-", filePart.filename()),
                        tempFile -> filePart.transferTo(tempFile)
                                .then(audioService.transcribeAudio(new FileSystemResource(tempFile))),
                        tempFile -> {
                            try {
                                Files.deleteIfExists(tempFile);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
        );
    }


    @PostMapping(value = "/generateAudio", produces = "audio/mpeg")
    Flux<byte[]> generateAudio(@RequestBody TextRequest request) {
        return audioService.generateAudio(request.message());
    }

}
