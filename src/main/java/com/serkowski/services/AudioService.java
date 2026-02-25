package com.serkowski.services;

import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class AudioService {

    private final TranscriptionModel transcriptionModel;
    private final OpenAiAudioSpeechModel openAiAudioSpeechModel;

    public AudioService(TranscriptionModel transcriptionModel, OpenAiAudioSpeechModel openAiAudioSpeechModel) {
        this.transcriptionModel = transcriptionModel;
        this.openAiAudioSpeechModel = openAiAudioSpeechModel;
    }

    public String transcribeAudio(Resource resource) {
        try {
            return transcriptionModel.transcribe(resource);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error during audio transcription", e);
        }
    }

    public byte[] generateAudio(String message) {
        var prompt = new TextToSpeechPrompt(message, OpenAiAudioSpeechOptions.builder()
                .voice(OpenAiAudioApi.SpeechRequest.Voice.FABLE)
                .build());
        return openAiAudioSpeechModel.call(prompt).getResult().getOutput();
    }
}

