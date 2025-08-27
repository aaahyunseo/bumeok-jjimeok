package com.example.bjjm.dto.response.home;

import com.example.bjjm.entity.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendThemeDto {
    private UUID themeId;
    private String title;
    private String mainImageUrl;

    public static RecommendThemeDto from(Theme theme) {
        return RecommendThemeDto.builder()
                .themeId(theme.getId())
                .title(theme.getTitle())
                .mainImageUrl(theme.getMainImageUrl())
                .build();
    }
}
