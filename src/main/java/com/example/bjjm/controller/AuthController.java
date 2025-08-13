package com.example.bjjm.controller;

import com.example.bjjm.dto.ResponseDto;
import com.example.bjjm.dto.response.kakao.TokenResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.bjjm.service.KakaoAuthService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final KakaoAuthService kakaoAuthService;

    @Operation(summary = "카카오 로그인", description = "프론트에서 받은 인가 코드를 이용해 JWT를 발급합니다.")
    @GetMapping("/kakao/callback")
    public ResponseEntity<ResponseDto<TokenResponseDto>> kakaoCallback(@RequestParam String code) throws IOException {
        TokenResponseDto tokens = kakaoAuthService.kakoLoginByCode(code);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "카카오 로그인 성공", tokens), HttpStatus.OK);
    }
}

