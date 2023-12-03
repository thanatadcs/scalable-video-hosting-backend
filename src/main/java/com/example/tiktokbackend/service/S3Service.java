package com.example.tiktokbackend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

@Slf4j
@Service
public class S3Service {

    private S3Presigner presigner;
    private S3Client s3Client;

    S3Service() {
        presigner = S3Presigner.create();
        s3Client = S3Client.create();
    }

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

    /*
     * Source: https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/examples-s3-presign.html
     */
    public URL getPresignedUrl(String bucketName, String keyName) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);
        return presignedGetObjectRequest.url();
    }

    /*
     * Source: https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/s3/src/main/java/com/example/s3/GetObjectData.java#L66
     */
    public String getObjectString(String bucketName, String keyName) {
        GetObjectRequest objectRequest = GetObjectRequest
                .builder()
                .key(keyName)
                .bucket(bucketName)
                .build();

        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(objectRequest);
        byte[] data = objectBytes.asByteArray();

        return new String(data, StandardCharsets.UTF_8);
    }

}
