package com.example.bjjm.dto.response.map;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceResponseDto {
    private String name;
    private String address;
    private String phone;
    private String rating;
    private List<String> imageUrls;
    private List<MenuDto> menus;
    private String link;
}
