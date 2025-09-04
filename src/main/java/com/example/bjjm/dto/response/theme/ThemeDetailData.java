package com.example.bjjm.dto.response.theme;

import com.example.bjjm.entity.Theme;
import com.example.bjjm.entity.ThemeImage;
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
    private List<String> mainImageUrls;
    private boolean scrapped;
    private String writer;
    private String writerProfile;
    private long viewCount;
    private List<String> keywords;
    private List<ThemeItemResponseDto> themeItems;

    public static ThemeDetailData from(Theme theme, boolean scrapped) {
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
                .mainImageUrls(theme.getMainImagesUrls().stream()
                        .map(ThemeImage::getImageUrl)
                        .toList())
                .scrapped(scrapped)
                .writer(theme.getUser().getName())
                .writerProfile(theme.getUser().getProfileImage())
                .viewCount(theme.getViewCount())
                .keywords(keywordNames)
                .themeItems(itemDtos)
                .build();
    }
}

