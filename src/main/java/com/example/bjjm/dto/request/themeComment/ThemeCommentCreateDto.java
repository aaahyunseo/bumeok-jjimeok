package com.example.bjjm.dto.request.themeComment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThemeCommentCreateDto {
    @NotBlank(message = "댓글을 입력해주세요.")
    @Size(max = 200)
    private String content;
}
