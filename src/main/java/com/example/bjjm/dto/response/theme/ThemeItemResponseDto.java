package com.example.bjjm.dto.response.theme;

import com.example.bjjm.entity.ThemeImage;
import com.example.bjjm.entity.ThemeItem;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ThemeItemResponseDto {
    private String content;
    private String address;
    private List<String> imageUrls;

    public static ThemeItemResponseDto from(ThemeItem item) {
        return ThemeItemResponseDto.builder()
                .content(item.getContent())
                .address(item.getAddress())
                .imageUrls(item.getImageFiles().stream()
                        .map(ThemeImage::getImageUrl)
                        .collect(Collectors.toList()))
                .build();
    }
}

