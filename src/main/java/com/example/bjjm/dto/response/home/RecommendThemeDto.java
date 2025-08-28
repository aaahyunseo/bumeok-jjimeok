package com.example.bjjm.dto.response.home;

import com.example.bjjm.entity.Theme;
import com.example.bjjm.entity.ThemeImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendThemeDto {
    private UUID themeId;
    private String title;
    private List<String> mainImageUrls;

    public static RecommendThemeDto from(Theme theme) {
        return RecommendThemeDto.builder()
                .themeId(theme.getId())
                .title(theme.getTitle())
                .mainImageUrls(theme.getMainImagesUrls().stream()
                        .map(ThemeImage::getImageUrl)
                        .toList())
                .build();
    }
}
