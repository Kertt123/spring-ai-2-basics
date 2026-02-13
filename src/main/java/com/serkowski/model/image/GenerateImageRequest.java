package com.serkowski.model.image;

public record GenerateImageRequest(
        String message,
        String quality,
        int height,
        int width
) {
}

