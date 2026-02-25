package com.serkowski.controller;

import com.serkowski.model.image.TextWithImgPathRequest;
import com.serkowski.model.image.TextWithImgUrlRequest;
import com.serkowski.model.text.TextRequest;
import com.serkowski.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/textWithImageUrl")
    String textWithImageUrl(@RequestBody TextWithImgUrlRequest request) {
        return chatService.getCompletionsWithImageUrl(request.message(), request.imageType(), request.imageUrl(), request.conversationId());
    }

    @PostMapping("/textWithImagePath")
    String textWithImagePath(@RequestBody TextWithImgPathRequest request) {
        return chatService.getCompletionsWithImagePath(request.message(), request.imageType(), request.imagePath(), request.conversationId());
    }

    @PostMapping(value = "/textWithImage", consumes = "multipart/form-data")
    String textWithImagePath(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") String requestJson) throws IOException {
        TextRequest requestData = new ObjectMapper().readValue(requestJson, TextRequest.class);
        byte[] bytes = file.getBytes();
        MediaType contentType = MediaTypeFactory.getMediaType(file.getOriginalFilename())
                .orElse(MediaType.IMAGE_PNG);
        return chatService.getCompletionsWithImage(
                requestData.message(),
                contentType.getType() + "/" + contentType.getSubtype(),
                bytes,
                requestData.conversationId());
    }
}
