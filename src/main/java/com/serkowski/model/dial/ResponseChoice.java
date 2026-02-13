package com.serkowski.model.dial;

public record ResponseChoice(ResponseMessage message, ResponseDelta delta, String finish_reason) {
}
