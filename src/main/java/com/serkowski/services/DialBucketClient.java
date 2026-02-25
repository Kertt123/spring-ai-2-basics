package com.serkowski.services;

import com.serkowski.model.bucket.BucketResponse;
import com.serkowski.model.bucket.BucketUploadResponse;
import com.serkowski.model.bucket.DialAttachement;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class DialBucketClient {

    RestClient restClient;
    String endpoint;
    String apiKey;

    public DialBucketClient(RestClient restClient, String endpoint, String apiKey) {
        this.restClient = restClient;
        this.endpoint = endpoint;
        this.apiKey = apiKey;
    }

    public DialAttachement putImageIntoDIALBucket(byte[] attachment, String fileName, String type) {
        BucketResponse bucketResponse = restClient.get()
                .uri(endpoint + "/v1/bucket")
                .header("api-key", apiKey)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(BucketResponse.class);

        MultiValueMap<String, Object> multipartBody = new LinkedMultiValueMap<>();
        ByteArrayResource fileResource = new ByteArrayResource(attachment) {
            @Override
            public String getFilename() {
                return fileName;
            }
        };
        multipartBody.add("file", fileResource);

        BucketUploadResponse bucketUploadResponse = restClient.put()
                .uri(endpoint + "/v1/files/" + bucketResponse.bucket() + "/" + fileName)
                .header("api-key", apiKey)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(multipartBody)
                .retrieve()
                .body(BucketUploadResponse.class);

        return new DialAttachement(fileName, bucketUploadResponse.url(), type);
    }

    public byte[] getAttachmentFromBucket(String url) {
        return restClient.get()
                .uri(endpoint + "/v1/" + url)
                .header("api-key", apiKey)
                .retrieve()
                .body(byte[].class);
    }
}
