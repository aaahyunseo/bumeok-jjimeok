package com.example.bjjm.dto.request.theme;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class ThemeCreateRequestDto {
    @NotBlank(message = "테마 제목을 입력해주세요.")
    @Size(max = 100)
    private String title;

    @NotBlank(message = "테마 한 줄 소개를 입력해주세요.")
    @Size(max = 500)
    private String introduction;

    @NotEmpty(message = "테마 키워드를 입력해주세요.")
    private List<String> keywords;

    private List<ThemeItemCreateDto> items;
}
