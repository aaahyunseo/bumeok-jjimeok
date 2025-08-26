package com.example.bjjm.dto.response.home;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Top3PlaceDto {
    private String placeName;
    private String address;
    private String imageUrl;
}
