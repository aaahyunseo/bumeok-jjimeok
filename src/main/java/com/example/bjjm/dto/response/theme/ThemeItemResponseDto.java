package com.example.bjjm.dto.response.theme;

import com.example.bjjm.entity.ThemeItem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ThemeItemResponseDto {
    private String content;
    private String address;
    private String imageUrl;

    public static ThemeItemResponseDto from(ThemeItem item) {
        return ThemeItemResponseDto.builder()
                .content(item.getContent())
                .address(item.getAddress())
                .imageUrl(item.getImageUrl())
                .build();
    }
}

