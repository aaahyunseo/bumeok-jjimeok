package com.example.bjjm.dto.response.tour;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourPlacePhotoDto {
    private String contentId;
    private String title;
    private String imageUrl;
    private String photographyMonth;
    private String photographyLocation;
    private String photographer;
    private String searchKeyword;
}
