package com.example.bjjm.dto.response.puzzle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MissionRankingResponseData {
    private List<MissionRankingResponseDto> missionRankings;

    public static MissionRankingResponseData from(List<MissionRankingResponseDto> dtos) {
        return MissionRankingResponseData.builder()
                .missionRankings(dtos)
                .build();
    }
}
