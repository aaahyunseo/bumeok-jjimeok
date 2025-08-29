package com.example.bjjm.service;

import com.example.bjjm.entity.*;
import com.example.bjjm.repository.MissionRecordImageRepository;
import com.example.bjjm.repository.PlaceReviewImageRepository;
import com.example.bjjm.repository.ThemeImageRepository;
import com.example.bjjm.repository.ThemeReviewImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final S3Service s3Service;
    private final ThemeImageRepository themeImageRepository;
    private final ThemeReviewImageRepository themeReviewImageRepository;
    private final MissionRecordImageRepository missionRecordImageRepository;
    private final PlaceReviewImageRepository placeReviewImageRepository;

    /**
     * 테마 이미지 업로드
     */
    @Transactional
    public List<ThemeImage> uploadThemeImages(Theme theme, List<MultipartFile> files) throws IOException {
        List<String> urls = s3Service.uploadThemeImages(files);
        List<ThemeImage> images = urls.stream()
                .map(url -> ThemeImage.builder()
                        .theme(theme)
                        .imageUrl(url)
                        .build())
                .toList();
        return themeImageRepository.saveAll(images);
    }

    @Transactional
    public void deleteThemeImages(Theme theme) {
        List<ThemeImage> files = new ArrayList<>(theme.getMainImagesUrls());
        if (!files.isEmpty()) {
            files.forEach(img -> s3Service.deleteImage(img.getImageUrl()));
            themeImageRepository.deleteAll(files);
            theme.getMainImagesUrls().clear();
        }
    }

    /**
     * 테마 리뷰 이미지 업로드
     */
    @Transactional
    public List<ThemeReviewImage> uploadThemeReviewImages(User user, ThemeReview review, List<MultipartFile> files) throws IOException {
        List<String> urls = s3Service.uploadThemeReviewImages(files);
        List<ThemeReviewImage> images = urls.stream()
                .map(url -> ThemeReviewImage.builder()
                        .themeReview(review)
                        .user(user)
                        .imageUrl(url)
                        .build())
                .toList();
        return themeReviewImageRepository.saveAll(images);
    }

    @Transactional
    public void deleteThemeReviewImages(ThemeReview review) {
        List<ThemeReviewImage> files = new ArrayList<>(review.getImageFiles());
        if (!files.isEmpty()) {
            files.forEach(img -> s3Service.deleteImage(img.getImageUrl()));
            themeReviewImageRepository.deleteAll(files);
            review.getImageFiles().clear();
        }
    }

    /**
     * 미션 기록 이미지 업로드
     */
    @Transactional
    public List<MissionRecordImage> uploadMissionImages(MissionRecord record, List<MultipartFile> files) throws IOException {
        List<String> urls = s3Service.uploadMissionImages(files);
        List<MissionRecordImage> images = urls.stream()
                .map(url -> MissionRecordImage.builder()
                        .missionRecord(record)
                        .imageUrl(url)
                        .build())
                .toList();
        return missionRecordImageRepository.saveAll(images);
    }

    @Transactional
    public void deleteMissionImages(MissionRecord record) {
        List<MissionRecordImage> files = new ArrayList<>(record.getImageFiles());
        if (!files.isEmpty()) {
            files.forEach(img -> s3Service.deleteImage(img.getImageUrl()));
            missionRecordImageRepository.deleteAll(files);
            record.getImageFiles().clear();
        }
    }

    /**
     * 장소 리뷰 이미지 업로드
     */
    @Transactional
    public List<PlaceReviewImage> uploadPlaceReviewImages(User user, PlaceReview review, List<MultipartFile> files) throws IOException {
        List<String> urls = s3Service.uploadPlaceReviewImages(files);
        List<PlaceReviewImage> images = urls.stream()
                .map(url -> PlaceReviewImage.builder()
                        .placeReview(review)
                        .user(user)
                        .imageUrl(url)
                        .build())
                .toList();
        return placeReviewImageRepository.saveAll(images);
    }

    @Transactional
    public void deletePlaceReviewImages(PlaceReview review) {
        List<PlaceReviewImage> files = new ArrayList<>(review.getImageFiles());
        if (!files.isEmpty()) {
            files.forEach(img -> s3Service.deleteImage(img.getImageUrl()));
            placeReviewImageRepository.deleteAll(files);
            review.getImageFiles().clear();
        }
    }
}
