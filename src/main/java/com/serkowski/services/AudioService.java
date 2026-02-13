package com.serkowski.services;

import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.core.io.ByteArrayResource;
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

    public Mono<String> transcribeAudio(byte[] audioData) {
        return Mono.defer(() -> {
            try {
                ByteArrayResource audioResource = new ByteArrayResource(audioData) {
                    @Override
                    public String getFilename() {
                        //bypass file name mandatory
                        return "audio.m4a";
                    }
                };
                String transcription = transcriptionModel.transcribe(audioResource);
                return Mono.just(transcription);
            } catch (Exception e) {
                return Mono.error(new IllegalArgumentException("Error during audio transcription", e));
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<byte[]> generateAudio(String message) {
        return openAiAudioSpeechModel.stream(message);
    }
}

