package com.serkowski.services;

import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class AudioService {

    private final TranscriptionModel transcriptionModel;
    private final OpenAiAudioSpeechModel openAiAudioSpeechModel;

    public AudioService(TranscriptionModel transcriptionModel, OpenAiAudioSpeechModel openAiAudioSpeechModel) {
        this.transcriptionModel = transcriptionModel;
        this.openAiAudioSpeechModel = openAiAudioSpeechModel;
    }

    public Mono<String> transcribeAudio(Resource resource) {
        return Mono.defer(() -> {
            try {
                String transcription = transcriptionModel.transcribe(resource);
                return Mono.just(transcription);
            } catch (Exception e) {
                return Mono.error(new IllegalArgumentException("Error during audio transcription", e));
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<byte[]> generateAudio(String message) {
        return openAiAudioSpeechModel.stream(message, OpenAiAudioSpeechOptions.builder()
                .voice(OpenAiAudioApi.SpeechRequest.Voice.FABLE)
                .build());
    }
}

