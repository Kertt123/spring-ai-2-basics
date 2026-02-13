package com.serkowski.model.dial;


import java.util.List;

public record Request(List<RequestMessage> messages, boolean stream) {
}
