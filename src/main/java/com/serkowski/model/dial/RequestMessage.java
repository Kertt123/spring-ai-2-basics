package com.serkowski.model.dial;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.serkowski.model.bucket.CustomContent;

import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RequestMessage(String role, String content, CustomContent custom_content, CustomField custom_fields) implements Serializable {
}
