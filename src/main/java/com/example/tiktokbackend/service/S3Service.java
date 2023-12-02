package com.example.tiktokbackend.service;

import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;
import java.util.Map;

public class S3Service {

    private S3Presigner presigner = S3Presigner.create();

    /**
     * Create a presigned URL for uploading with a PUT request.
     *
     * @param bucketName  - The name of the bucket.
     * @param keyName     - The name of the object.
     * @param contentType - The content type of the object.
     * @param metadata    - The metadata to store with the object.
     * @return - The presigned URL for an HTTP PUT.
     * <p>
     * Source: https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/examples-s3-presign.html
     */
    public URL createPresignedUploadUrl(String bucketName, String keyName, String contentType, Map<String, String> metadata) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .contentType(contentType)
                .metadata(metadata)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))  // The URL will expire in 10 minutes.
                .putObjectRequest(objectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);

        return presignedRequest.url();
    }
}
