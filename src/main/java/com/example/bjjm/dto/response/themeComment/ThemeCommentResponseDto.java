package com.example.bjjm.dto.response.themeComment;

import com.example.bjjm.entity.ThemeComment;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Builder
public class ThemeCommentResponseDto {
    private UUID commentId;
    private String writer;
    private String content;
    private String createdAt;

    public static ThemeCommentResponseDto from(ThemeComment comment) {
        return ThemeCommentResponseDto.builder()
                .commentId(comment.getId())
                .writer(comment.getUser().getName())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();
    }
}
