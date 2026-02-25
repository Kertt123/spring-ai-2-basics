package com.serkowski.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serkowski.model.image.GenerateImageRequest;
import com.serkowski.model.image.TextWithImgPathRequest;
import com.serkowski.model.image.TextWithImgUrlRequest;
import com.serkowski.model.text.TextRequest;
import com.serkowski.services.ImageService;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/textWithImageUrl")
    String textWithImageUrl(@RequestBody TextWithImgUrlRequest request) {
        return imageService.getCompletionsWithImageUrl(request.message(), request.imageType(), request.imageUrl(), request.conversationId());
    }

    @PostMapping("/textWithImagePath")
    String textWithImagePath(@RequestBody TextWithImgPathRequest request) {
        return imageService.getCompletionsWithImagePath(request.message(), request.imageType(), request.imagePath(), request.conversationId());
    }

    @PostMapping(value = "/textWithImage", consumes = "multipart/form-data")
    String textWithImage(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") String requestJson) {
        try {
            TextRequest requestData = new ObjectMapper().readValue(requestJson, TextRequest.class);
            byte[] bytes = file.getBytes();
            MediaType contentType = MediaTypeFactory.getMediaType(file.getResource())
                    .orElse(MediaType.IMAGE_PNG);
            return imageService.getCompletionsWithImage(requestData.message(), contentType.getType() + "/" + contentType.getSubtype(), bytes, requestData.conversationId());
        } catch (Exception e) {
             throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "/generateImage", produces = MediaType.IMAGE_PNG_VALUE)
    ResponseEntity<byte[]> generateImage(@RequestBody GenerateImageRequest request) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imageService.generateImage(request.message(), request.quality(), request.height(), request.width()));
    }
}
