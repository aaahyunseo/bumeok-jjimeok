package com.example.bjjm.dto.response.map;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceResponseDto {
    private String placeName;
    private String address;
    private String tel;
    private String mainMenu;
    private String otherMenu;
    private String usageTime;
    private String holiday;
    private String content;
    private String mainImageUrl;
    private double scoreAvg;
    private int reviewCount;
}
