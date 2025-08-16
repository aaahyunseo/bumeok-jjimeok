package com.example.bjjm.dto.response.theme;

import com.example.bjjm.entity.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class ThemeListResponseData {
    private List<ThemeListResponseDto> themeList;

    public static ThemeListResponseData from(List<Theme> themes) {
        return ThemeListResponseData.builder()
                .themeList(
                        themes.stream()
                                .map(ThemeListResponseDto::from)
                                .collect(Collectors.toList()))
                .build();
    }
}
