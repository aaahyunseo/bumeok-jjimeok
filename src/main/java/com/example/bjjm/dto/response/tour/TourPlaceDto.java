package com.example.bjjm.dto.response.tour;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourPlaceDto {
    private String title;
    private String address;
    private String tel;
    private String mapX;
    private String mapY;
    private String dist;
    private String firstImage;
}
