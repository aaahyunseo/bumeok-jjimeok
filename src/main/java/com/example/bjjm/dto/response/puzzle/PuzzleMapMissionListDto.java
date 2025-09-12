package com.example.bjjm.dto.response.puzzle;

import com.example.bjjm.entity.Mission;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PuzzleMapMissionListDto {
    // 상세 미션 ID
    private UUID missionId;
    // 미션 제목
    private String missionTitle;
    // 미션 한줄 소개
    private String missionDescription;
    // 미션 수행 여부
    @JsonProperty("isCompleted")
    private boolean isCompleted;

    public static PuzzleMapMissionListDto of(Mission mission, boolean isCompleted) {
        return PuzzleMapMissionListDto.builder()
                .missionId(mission.getId())
                .missionTitle(mission.getTitle())
                .missionDescription(mission.getIntroduction())
                .isCompleted(isCompleted)
                .build();
    }
}
