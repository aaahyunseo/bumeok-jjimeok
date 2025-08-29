package com.example.bjjm.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3client;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    // 단일 이미지 파일 업로드
    private String uploadToFolder(MultipartFile file, String folderName) throws IOException {
        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new IllegalStateException("파일명이 null입니다.");
        }

        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex == -1) {
            throw new IllegalArgumentException("파일에 적절한 확장자가 없습니다: " + filename);
        }

        String key = folderName + "/" + changedImageName(filename);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        s3client.putObject(bucketName, key, file.getInputStream(), metadata);

        return generateFileUrl(key);
    }

    // 다중 이미지 파일 업로드
    private List<String> uploadToFolder(List<MultipartFile> files, String folderName) throws IOException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            urls.add(uploadToFolder(file, folderName));
        }
        return urls;
    }

    // 이미지 삭제
    public void deleteImage(String imageUrl) {
        String splitStr = ".com/";
        String key = imageUrl.substring(imageUrl.lastIndexOf(splitStr) + splitStr.length());
        s3client.deleteObject(new DeleteObjectRequest(bucketName, key));
    }

    // 랜덤 파일 이름 메서드
    private String changedImageName(String originName) {
        String uuid = UUID.randomUUID().toString();
        String extension = originName.substring(originName.lastIndexOf("."));
        return uuid + extension;
    }

    private String generateFileUrl(String key) {
        return s3client.getUrl(bucketName.trim(), key).toString();
    }

    // 테마 이미지 업로드
    public List<String> uploadThemeImages(List<MultipartFile> files) throws IOException {
        return uploadToFolder(files, "themes");
    }

    // 테마 단일 파일 업로드
    public String uploadThemeImage(MultipartFile file) throws IOException {
        return uploadToFolder(file, "themes");
    }

    // 테마 리뷰 이미지 업로드
    public List<String> uploadThemeReviewImages(List<MultipartFile> files) throws IOException {
        return uploadToFolder(files, "theme-reviews");
    }

    // 미션 기록 이미지 업로드
    public List<String> uploadMissionImages(List<MultipartFile> files) throws IOException {
        return uploadToFolder(files, "missions");
    }

    // 장소 리뷰 이미지 업로드
    public List<String> uploadPlaceReviewImages(List<MultipartFile> files) throws IOException {
        return uploadToFolder(files, "place-reviews");
    }

    // 뱃지 URL 생성
    public String getBadgeImageUrl(String badgeCode) {
        String key = "badges/" + badgeCode + ".png";
        return generateFileUrl(key);
    }
}
