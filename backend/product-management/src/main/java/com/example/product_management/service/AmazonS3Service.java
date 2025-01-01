package com.example.product_management.service;

import com.example.product_management.model.AwsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.StorageClass;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Service
public class AmazonS3Service {

    private final S3Client s3Client;
    private final AwsProperties awsProperties;

    public AmazonS3Service(@Qualifier("s3Client") S3Client s3Client, AwsProperties awsProperties) {
        this.s3Client = s3Client;
        this.awsProperties = awsProperties;
    }

    public String uploadFile(MultipartFile multipartFile, Integer id, String type) {
        try {
            System.out.println("Uploading file to S3...");
            String bucketName = awsProperties.getS3().getBucket();
            String fileExtension = Objects.requireNonNull(multipartFile.getOriginalFilename()).substring(multipartFile.getOriginalFilename().lastIndexOf('.'));
            String folder = type.equals("category") ? "categories" : "products";
            String fileName = folder + "/" + type + "_" + id + fileExtension;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentLength(multipartFile.getSize())
                    .storageClass(StorageClass.STANDARD)
                    .build();

            s3Client.putObject(putObjectRequest,
                    RequestBody.fromBytes(multipartFile.getInputStream().readAllBytes()));

            return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(fileName)).toExternalForm();
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}