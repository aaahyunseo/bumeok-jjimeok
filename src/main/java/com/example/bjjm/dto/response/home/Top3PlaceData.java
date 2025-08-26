package com.example.bjjm.dto.response.home;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Top3PlaceData {
    private List<Top3PlaceDto> top3Places;

    public static Top3PlaceData from(List<Top3PlaceDto> top3Places) {
        return Top3PlaceData.builder()
                .top3Places(top3Places)
                .build();
    }
}
