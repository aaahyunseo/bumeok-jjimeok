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
public class CustomizedThemeDto {
    private UUID themeId;
    private String title;
    private String introduction;
    private String mainImageUrl;

    public static CustomizedThemeDto from(Theme theme) {
        return CustomizedThemeDto.builder()
                .themeId(theme.getId())
                .title(theme.getTitle())
                .introduction(theme.getIntroduction())
                .mainImageUrl(theme.getMainImageUrl())
                .build();
    }
}
