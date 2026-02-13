package com.serkowski.model.image;

public record TextWithImgUrlRequest(String message, String imageType, String imageUrl, String conversationId) {
}
