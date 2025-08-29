package com.example.bjjm.dto.request.theme;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThemeItemCreateDto {
    @NotBlank(message = "테마 소개 내용을 입력하세요.")
    @Size(max = 2000)
    private String content;

    @NotBlank(message = "테마 주소를 입력하세요.")
    private String address;

    @NotBlank(message = "테마 소개 이미지를 입력하세요.")
    private MultipartFile imageFile;
}
