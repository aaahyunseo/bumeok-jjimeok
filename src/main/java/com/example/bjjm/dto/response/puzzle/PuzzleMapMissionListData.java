package com.example.bjjm.dto.response.puzzle;

import com.example.bjjm.entity.Mission;
import com.example.bjjm.entity.UserPuzzle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PuzzleMapMissionListData {
    private String regionName;
    private List<PuzzleMapMissionListDto> missions;

    public static PuzzleMapMissionListData of(List<Mission> missions, UserPuzzle userPuzzle) {
        List<PuzzleMapMissionListDto> missionListDtos = missions.stream()
                .map(PuzzleMapMissionListDto::from)
                .collect(Collectors.toList());

        return PuzzleMapMissionListData.builder()
                .regionName(userPuzzle.getPuzzle().getRegion())
                .missions(missionListDtos)
                .build();
    }

}
