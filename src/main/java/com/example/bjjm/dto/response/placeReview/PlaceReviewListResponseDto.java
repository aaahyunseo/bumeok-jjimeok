package com.example.bjjm.dto.response.placeReview;

import com.example.bjjm.entity.PlaceReview;
import com.example.bjjm.entity.PlaceReviewImage;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class PlaceReviewListResponseDto {
    // 유저 이름
    private String userName;
    // 유저 프로필 사진
    private String userProfileImageUrl;
    // 리뷰 내용
    private String content;
    // 리뷰 사진
    private List<String> placeReviewImageUrls;
    // 별점
    private int score;
    // 작성일
    private String createdAt;

    public static PlaceReviewListResponseDto from(PlaceReview placeReview) {
        List<String> imageUrls = placeReview.getImageFiles().stream()
                .map(PlaceReviewImage::getImageUrl)
                .collect(Collectors.toList());

        return PlaceReviewListResponseDto.builder()
                .userName(placeReview.getUser().getName())
                .userProfileImageUrl(placeReview.getUser().getProfileImage())
                .content(placeReview.getContent())
                .placeReviewImageUrls(imageUrls)
                .score(placeReview.getScore())
                .createdAt(placeReview.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();
    }
}
