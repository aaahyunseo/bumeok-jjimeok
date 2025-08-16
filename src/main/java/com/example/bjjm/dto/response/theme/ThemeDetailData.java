package com.example.bjjm.dto.response.theme;

import com.example.bjjm.entity.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThemeDetailData {
    private UUID themeId;
    private String title;
    private String introduction;
    private String writer;
    private long viewCount;
    private List<String> keywords;
    private List<ThemeItemResponseDto> themeItems;

    public static ThemeDetailData from(Theme theme) {
        List<ThemeItemResponseDto> itemDtos = theme.getThemeItems().stream()
                .map(ThemeItemResponseDto::from)
                .collect(Collectors.toList());

        List<String> keywordNames = theme.getKeywords().stream()
                .map(themeKeyword -> themeKeyword.getKeyword().getDisplayName())
                .collect(Collectors.toList());

        return ThemeDetailData.builder()
                .themeId(theme.getId())
                .title(theme.getTitle())
                .introduction(theme.getIntroduction())
                .writer(theme.getUser().getName())
                .viewCount(theme.getViewCount())
                .keywords(keywordNames)
                .themeItems(itemDtos)
                .build();
    }
}

