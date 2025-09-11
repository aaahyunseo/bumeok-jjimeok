package com.example.bjjm.dto.response.puzzle;

import com.example.bjjm.entity.Mission;
import com.example.bjjm.entity.Puzzle;
import com.example.bjjm.entity.User;
import com.example.bjjm.repository.MissionRecordRepository;
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

    public static PuzzleMapMissionListData of(List<Mission> missions, Puzzle puzzle, User user, MissionRecordRepository missionRecordRepository) {
        List<PuzzleMapMissionListDto> missionListDtos = missions.stream()
                .map(mission -> {
                    boolean isCompleted = missionRecordRepository.existsByMissionAndUser(mission, user);
                    return PuzzleMapMissionListDto.of(mission, isCompleted);
                })
                .collect(Collectors.toList());

        return PuzzleMapMissionListData.builder()
                .regionName(puzzle.getRegion())
                .missions(missionListDtos)
                .build();
    }

}
