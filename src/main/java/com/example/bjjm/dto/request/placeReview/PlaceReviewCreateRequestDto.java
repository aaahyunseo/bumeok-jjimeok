package com.example.bjjm.dto.request.placeReview;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceReviewCreateRequestDto {
    private String content;
    private String address;
    private String placeName;
    private double score;
}
