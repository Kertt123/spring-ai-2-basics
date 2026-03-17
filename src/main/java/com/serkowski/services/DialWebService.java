package com.serkowski.services;

import com.serkowski.model.bucket.CustomContent;
import com.serkowski.model.bucket.DialAttachement;
import com.serkowski.model.dial.ConfigurationRequest;
import com.serkowski.model.dial.CustomField;
import com.serkowski.model.dial.Request;
import com.serkowski.model.dial.RequestMessage;
import com.serkowski.model.dial.Response;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class DialWebService {

    RestClient restClient;
    String url;
    String apiKey;
    DialBucketClient dialBucketClient;

    public DialWebService(RestClient restClient, String url, String apiKey, DialBucketClient dialBucketClient) {
        this.restClient = restClient;
        this.url = url;
        this.apiKey = apiKey;
        this.dialBucketClient = dialBucketClient;
    }

    public String getCompletionsWithImagePathDIAL(String message, String imgType, String imgPath) {
        try {
            ClassPathResource resource = new ClassPathResource(imgPath);
            byte[] contentAsByteArray = resource.getContentAsByteArray();
            DialAttachement dialAttachement = dialBucketClient.putImageIntoDIALBucket(contentAsByteArray, resource.getFilename(), imgType);
            return getCompletions(message, dialAttachement);
        } catch (IOException e) {
            throw new IllegalArgumentException("Not correct image path " + imgPath, e);
        }
    }

    public String getCompletions(String message, DialAttachement dialAttachement) {
        Response response = restClient
                .post()
                .uri(url + "/openai/deployments/gpt-4o/chat/completions")
                .header("api-key", apiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(new Request(List.of(new RequestMessage("user", message, new CustomContent(List.of(dialAttachement)), null)), false))
                .retrieve()
                .body(Response.class);

        String content = response.choices().stream()
                .findFirst()
                .map(choice -> choice.message().content())
                .orElse("");

        System.out.println(content);
        return content;
    }

    public byte[] generateImage(String prompt, String size, String style, String quality) {
        Response response = restClient
                .post()
                .uri(url + "/openai/deployments/gpt-image-1-mini-2025-10-06/chat/completions")
                .header("api-key", apiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(new Request(List.of(new RequestMessage("user", prompt, null, new CustomField(new ConfigurationRequest(size, style, quality)))), false))
                .retrieve()
                .body(Response.class);

        try {
            String imageUrl = response.choices().stream()
                    .findFirst()
                    .get()
                    .message()
                    .custom_content()
                    .attachments()
                    .stream()
                    .map(DialAttachement::url)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .get();
            return dialBucketClient.getAttachmentFromBucket(imageUrl);
        } catch (Exception e) {
            return new byte[0];
        }
    }
}
