package com.example.bjjm.dto.response.tour;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FestivalDto {
    private String address;
    private String mapX;
    private String mapY;
    private String firstImage;
    private String tel;
    private String title;
    private String eventStartDate;
    private String eventEndDate;
}
