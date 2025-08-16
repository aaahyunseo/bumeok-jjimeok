package com.example.bjjm.dto.response.themeReview;

import com.example.bjjm.entity.ThemeReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class ThemeReviewListData {
    private List<ThemeReviewListResponseDto> reviewList;

    public static ThemeReviewListData from(List<ThemeReview> themeReviews) {
        return ThemeReviewListData.builder()
                .reviewList(
                        themeReviews.stream()
                                .map(ThemeReviewListResponseDto::from)
                                .collect(Collectors.toList())
                )
                .build();
    }
}