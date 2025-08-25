package com.example.bjjm.dto.response.placeReview;

import com.example.bjjm.entity.PlaceReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class PlaceReviewListResponseData {
    List<PlaceReviewListResponseDto> placeReviewList;

    public static PlaceReviewListResponseData from(List<PlaceReview> placeReviews) {
        return PlaceReviewListResponseData.builder()
                .placeReviewList(
                        placeReviews.stream()
                                .map(PlaceReviewListResponseDto::from)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
