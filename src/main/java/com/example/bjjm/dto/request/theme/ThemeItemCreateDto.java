package com.example.bjjm.dto.request.theme;

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
public class ThemeItemCreateDto {
    @NotBlank(message = "테마 내용 입력하세요.")
    @Size(max = 1000)
    private String content;

    private String address;

    private List<String> imageUrls;
}
