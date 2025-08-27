package com.example.bjjm.dto.response.home;

import com.example.bjjm.entity.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendThemeData {
    private List<RecommendThemeDto> recommendThemes;

    public static RecommendThemeData from(List<Theme> themes) {
        return RecommendThemeData.builder()
                .recommendThemes(
                        themes.stream()
                                .map(RecommendThemeDto::from)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
