package ch.zhaw.policysign.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;

@Service
public class S3Service {

    @Autowired
    private S3Client s3Client;

    public String uploadFileToS3(MultipartFile file, String bucketName, String fileName) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        if (response.sdkHttpResponse().isSuccessful()) {
            return fileName;
        } else {
            throw new RuntimeException("Failed to upload file to S3");
        }
    }

    public String uploadFileToS3(byte[] fileBytes, String bucketName, String fileName) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType("application/pdf")
                .build();

        PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileBytes));

        if (response.sdkHttpResponse().isSuccessful()) {
            return fileName;
        } else {
            throw new RuntimeException("Failed to upload file to S3");
        }
    }

    public byte[] downloadFileFromS3(String fileName, String bucketName) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        try (ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest)) {
            return s3Object.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to download file from S3", e);
        }
    }
}
