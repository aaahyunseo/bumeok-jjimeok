package com.example.bjjm.dto.response.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KakaoProfile {
    @JsonProperty("nickname")
    private String name;

    @JsonProperty("profile_image_url")
    private String profileImage;
}
