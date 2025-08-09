package com.example.bjjm.service;

import com.example.bjjm.authentication.AccessTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.bjjm.dto.response.kakao.KakaoUserInfoResponse;
import com.example.bjjm.dto.response.kakao.TokenResponseDto;
import com.example.bjjm.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.bjjm.repository.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {
    private final UserRepository userRepository;
    private final AccessTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public TokenResponseDto kakaoLogin(String accessToken) throws IOException {
        // 1. 카카오 API 호출
        KakaoUserInfoResponse kakaoUser = getUserInfoFromKakao(accessToken);

        // 2. 유저 저장 or 조회
        User user = userRepository.findByKakaoId(kakaoUser.getKakaoUserId())
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .kakaoId(kakaoUser.getKakaoUserId())
                            .name(kakaoUser.getKakaoAccount().getProfile().getName())
//                            .email(kakaoUser.getKakaoAccount().getEmail())
                            .profileImage(kakaoUser.getKakaoAccount().getProfile().getProfileImage())
                            .build();
                    return userRepository.save(newUser);
                });

        // 3. JWT 발급
        return new TokenResponseDto(jwtTokenProvider.createToken(user.getId().toString()));
    }

    private KakaoUserInfoResponse getUserInfoFromKakao(String accessToken) throws IOException {
        HttpURLConnection connection = (HttpURLConnection)
                new URL("https://kapi.kakao.com/v2/user/me").openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        InputStream responseStream = connection.getInputStream();

        return objectMapper.readValue(responseStream, KakaoUserInfoResponse.class);
    }
}
