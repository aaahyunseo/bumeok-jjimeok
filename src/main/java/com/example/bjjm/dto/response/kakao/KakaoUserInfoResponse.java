package com.example.bjjm.dto.response.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KakaoUserInfoResponse {
    @JsonProperty("id")
    private Long kakaoUserId;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;
}
