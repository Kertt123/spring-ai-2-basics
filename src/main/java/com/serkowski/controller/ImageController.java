package com.serkowski.controller;

import com.serkowski.model.image.GenerateImageRequest;
import com.serkowski.model.image.TextWithImgPathRequest;
import com.serkowski.model.image.TextWithImgUrlRequest;
import com.serkowski.model.text.TextRequest;
import com.serkowski.services.ImageService;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/image")
public class ImageController {

    private final ImageService chatService;

    public ImageController(ImageService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/textWithImageUrl")
    Mono<String> textWithImageUrl(@RequestBody Mono<TextWithImgUrlRequest> requestBody) {
        return requestBody.flatMap(request -> chatService.getCompletionsWithImageUrl(request.message(), request.imageType(), request.imageUrl(), request.conversationId()));
    }

    @PostMapping("/textWithImagePath")
    Mono<String> textWithImagePath(@RequestBody Mono<TextWithImgPathRequest> requestBody) {
        return requestBody.flatMap(request -> chatService.getCompletionsWithImagePath(request.message(), request.imageType(), request.imagePath(), request.conversationId()));
    }

    @PostMapping(value = "/textWithImage", consumes = "multipart/form-data")
    Mono<String> textWithImagePath(
            @RequestPart("file") Mono<FilePart> file,
            @RequestPart("data") Mono<String> request) {
        return Mono.zip(file, request)
                .flatMap(tuple -> {
                    FilePart fileData = tuple.getT1();
                    TextRequest requestData = new ObjectMapper().readValue(tuple.getT2(), TextRequest.class);
                    return DataBufferUtils.join(fileData.content())
                            .flatMap(dataBuffer -> {
                                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(bytes);
                                DataBufferUtils.release(dataBuffer);
                                MediaType contentType = MediaTypeFactory.getMediaType(fileData.filename())
                                        .orElse(MediaType.IMAGE_PNG);
                                return chatService.getCompletionsWithImage(requestData.message(), contentType.getType() + "/" + contentType.getSubtype(), bytes, requestData.conversationId());
                            });
                });
    }

    @PostMapping(value = "/generateImage", produces = MediaType.IMAGE_PNG_VALUE)
    Mono<ResponseEntity<byte[]>> generateImage(@RequestBody GenerateImageRequest request) {
        return chatService.generateImage(request.message(), request.quality(), request.height(), request.width())
                .map(imageBytes -> ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(imageBytes));
    }
}
