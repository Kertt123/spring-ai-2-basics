package com.serkowski.model.dial;

import com.serkowski.model.bucket.CustomContent;

public record ResponseMessage(String content, CustomContent custom_content) {
}
