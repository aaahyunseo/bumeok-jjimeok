package com.example.bjjm.dto.response.theme;

import com.example.bjjm.entity.Theme;
import com.example.bjjm.entity.ThemeItem;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Builder
public class ThemeListResponseDto {
    // 테마 아이디
    private UUID themeId;
    // 테마 제목
    private String title;
    // 테마 한줄 소개
    private String introduction;
    // 테마 대표 이미지
    private String mainImageUrl;
    // 테마 등록 날짜
    private String createdAt;
    // 조회 횟수
    private long viewCount;

    public static ThemeListResponseDto from(Theme theme) {
        return ThemeListResponseDto.builder()
                .themeId(theme.getId())
                .title(theme.getTitle())
                .introduction(theme.getIntroduction())
                .mainImageUrl(theme.getMainImageUrl())
                .createdAt(theme.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .viewCount(theme.getViewCount())
                .build();
    }
}
