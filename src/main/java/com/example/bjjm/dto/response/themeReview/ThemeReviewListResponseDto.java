package com.example.bjjm.dto.response.themeReview;

import com.example.bjjm.entity.ThemeReview;
import com.example.bjjm.entity.ThemeReviewImage;
import com.example.bjjm.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
public class ThemeReviewListResponseDto {
    // 테마 리뷰 ID
    private UUID themeReviewId;
    // 테마 리뷰 내용
    private String content;
    // 테마 리뷰 작성자
    private String writer;
    // 테마 리뷰 작성자 프로필 이미지 URL
    private String writerProfile;
    // 테마 리뷰 작성일
    private String createdAt;
    // 테마 리뷰 사진 리스트
    private List<String> imageUrls;

    public static ThemeReviewListResponseDto from(ThemeReview themeReview) {
        List<String> imageUrls = themeReview.getImageFiles().stream()
                .map(ThemeReviewImage::getImageUrl)
                .collect(Collectors.toList());

        return ThemeReviewListResponseDto.builder()
                .themeReviewId(themeReview.getId())
                .content(themeReview.getContent())
                .writer(themeReview.getUser().getName())
                .writerProfile(themeReview.getUser().getProfileImage())
                .createdAt(themeReview.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .imageUrls(imageUrls)
                .build();
    }
}
