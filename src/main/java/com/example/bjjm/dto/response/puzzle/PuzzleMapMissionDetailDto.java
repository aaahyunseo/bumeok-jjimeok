package com.example.bjjm.dto.response.puzzle;

import com.example.bjjm.entity.Mission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PuzzleMapMissionDetailDto {
    // 미션 ID
    private UUID missionId;
    // 미션 제목
    private String missionTitle;
    // 미션 한 줄 소개
    private String missionIntroduction;
    // 미션 내용
    private String missionContent;
    // 미션 이미지
    private String missionImageUrl;
    // 미션 장소 x 좌표
    private String x;
    // 미션 장소 y 좌표
    private String y;

    public static PuzzleMapMissionDetailDto from(Mission mission) {
        return PuzzleMapMissionDetailDto.builder()
                .missionId(mission.getId())
                .missionTitle(mission.getTitle())
                .missionIntroduction(mission.getIntroduction())
                .missionContent(mission.getContent())
                .missionImageUrl(mission.getImageUrl())
                .x(mission.getX())
                .y(mission.getY())
                .build();
    }
}
