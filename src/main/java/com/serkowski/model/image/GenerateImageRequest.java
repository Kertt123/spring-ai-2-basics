package com.serkowski.model.image;

public record GenerateImageRequest(
        String message,
        String size,
        String style,
        String quality
) {
}

