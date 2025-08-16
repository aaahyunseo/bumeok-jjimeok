package com.example.bjjm.dto.response.themeComment;

import com.example.bjjm.entity.ThemeComment;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ThemeCommentListData {
    private List<ThemeCommentResponseDto> commentList;

    public static ThemeCommentListData from(List<ThemeComment> themeComments) {
        return ThemeCommentListData.builder()
                .commentList( themeComments.stream()
                        .map(ThemeCommentResponseDto::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
