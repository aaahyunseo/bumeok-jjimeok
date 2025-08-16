package com.example.bjjm.dto.request.themeReview;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThemeReviewCreateDto {
    @NotBlank(message = "테마 리뷰 글을 입력해주세요.")
    @Size(max = 1000)
    private String content;

    private List<String> imageUrls;
}
