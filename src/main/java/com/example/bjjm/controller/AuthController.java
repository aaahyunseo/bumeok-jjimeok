package com.example.bjjm.controller;

import com.example.bjjm.dto.ResponseDto;
import com.example.bjjm.dto.request.kakao.KakaoLoginRequest;
import com.example.bjjm.dto.response.kakao.TokenResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.bjjm.service.KakaoAuthService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final KakaoAuthService kakaoAuthService;

    @Operation(summary = "카카오 로그인", description = "프론트에서 받은 카카오 액세스 토큰을 통해 JWT를 발급합니다.")
    @PostMapping("/kakao/login")
    public ResponseEntity<ResponseDto<TokenResponseDto>> kakaoLogin(@RequestBody KakaoLoginRequest request) throws IOException {
        TokenResponseDto tokens = kakaoAuthService.kakaoLogin(request.getAccessToken());
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "카카오 로그인 성공", tokens), HttpStatus.OK);
    }

}

