package com.serkowski.controller;

import com.serkowski.model.image.GenerateImageRequest;
import com.serkowski.model.image.TextWithImgPathRequest;
import com.serkowski.services.DialWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/imageDial")
public class DialImageController {

    @Autowired
    private DialWebService dialWebService;


    @PostMapping("/textWithImagePathDial")
    Mono<String> textWithImagePathDial(@RequestBody Mono<TextWithImgPathRequest> requestBody) {
        return requestBody.flatMap(request -> dialWebService.getCompletionsWithImagePathDIAL(request.message(), request.imageType(), request.imagePath()));
    }

    @PostMapping(value = "/generateImage", produces = MediaType.IMAGE_PNG_VALUE)
    Mono<ResponseEntity<byte[]>> generateImage(@RequestBody GenerateImageRequest request) {
        return dialWebService.generateImage(request.message(), request.size(), request.style(), request.quality())
                .map(imageBytes -> ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(imageBytes));
    }


}
