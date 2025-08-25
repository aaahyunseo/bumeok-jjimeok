package com.example.bjjm.dto.response.puzzle;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MissionRankingResponseDto {
    private String userName;
    private String profileImage;
    private long successMissionCount;
    private int rank;
}

