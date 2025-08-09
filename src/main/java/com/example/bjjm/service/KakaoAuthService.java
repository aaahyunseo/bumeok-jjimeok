package com.example.bjjm.service;

import com.example.bjjm.authentication.AccessTokenProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.bjjm.dto.response.kakao.KakaoUserInfoResponse;
import com.example.bjjm.dto.response.kakao.TokenResponseDto;
import com.example.bjjm.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.bjjm.repository.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {
    private final UserRepository userRepository;
    private final AccessTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Value("${kakao.api.key}")
    private String kakaoRestApiKey;

    @Value("${kakao.redirect.uri}")
    private String kakaoRedirectUri;

    /**
     *  인가 코드로 JWT 발급
     * */
    public TokenResponseDto kakoLoginByCode(String code) throws IOException {
        // 1. 인가 코드로 Access Token 요청
        String kakaoAccessToken = getAccessTokenFromKakao(code);

        // 2. 카카오 API로 사용자 정보 조회
        KakaoUserInfoResponse kakaoUser = getUserInfoFromKakao(kakaoAccessToken);

        // 3. 유저 저장 or 기존 유저 조회
        User user = userRepository.findByKakaoId(kakaoUser.getKakaoUserId())
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .kakaoId(kakaoUser.getKakaoUserId())
                            .name(kakaoUser.getKakaoAccount().getProfile().getName())
                            .profileImage(kakaoUser.getKakaoAccount().getProfile().getProfileImage())
                            .build();
                    return userRepository.save(newUser);
                });

        // 4. JWT 발급
        return new TokenResponseDto(jwtTokenProvider.createToken(kakaoAccessToken));
    }

    /**
    *  카카오 Access Token 발급 요청
    * */
    private String getAccessTokenFromKakao(String code) throws IOException {
        URL url = new URL("https://kauth.kakao.com/oauth/token");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        String body = "grant_type=authorization_code" +
                "&client_id=" + kakaoRestApiKey +
                "&redirect_uri=" + kakaoRedirectUri +
                "&code=" + code;

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = body.getBytes();
            os.write(input, 0, input.length);
        }

        try (InputStream responseStream = connection.getInputStream()) {
            JsonNode jsonNode = objectMapper.readTree(responseStream);
            return jsonNode.get("access_token").asText();
        }
    }

    /**
     * 카카오 사용자 정보 요청
     */
    private KakaoUserInfoResponse getUserInfoFromKakao(String accessToken) throws IOException {
        HttpURLConnection connection = (HttpURLConnection)
                new URL("https://kapi.kakao.com/v2/user/me").openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        try (InputStream responseStream = connection.getInputStream()) {
            return objectMapper.readValue(responseStream, KakaoUserInfoResponse.class);
        }
    }
}
