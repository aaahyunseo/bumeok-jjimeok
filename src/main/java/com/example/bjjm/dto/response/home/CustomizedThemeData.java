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
public class CustomizedThemeData {
    private List<CustomizedThemeDto> themes;

    public static CustomizedThemeData from(List<Theme> themes) {
        return CustomizedThemeData.builder()
                .themes(
                        themes.stream()
                                .map(CustomizedThemeDto::from)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
