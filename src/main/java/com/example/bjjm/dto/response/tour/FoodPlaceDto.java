package com.example.bjjm.dto.response.tour;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodPlaceDto {
    private String title;
    private String place;
    private double mapX;
    private double mapY;
    private String address;
    private String tel;
    private String homepageUrl;
    private String usageTime;
    private String mainMenu;
    private String mainImage;
    private String content;
}
