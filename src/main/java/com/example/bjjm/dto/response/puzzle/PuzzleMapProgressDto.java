package com.example.bjjm.dto.response.puzzle;

import com.example.bjjm.entity.Puzzle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PuzzleMapProgressDto {
    // 퍼즐 지역 아이디
    private UUID puzzleRegionId;
    // 퍼즐 지역 이름
    private String puzzleRegion;
    // 해당 지역 미션 총 개수
    private int totalMissionCount;
    // 해당 지역 미션 완료 개수
    private int collectedMissionCount;
    // 해당 지역 퍼즐 완료 여부
    private boolean puzzleCompleted;

    public static PuzzleMapProgressDto from(Puzzle puzzle) {
        return PuzzleMapProgressDto.builder()
                .puzzleRegionId(puzzle.getPuzzleRegion().getId())
                .puzzleRegion(puzzle.getPuzzleRegion().getRegion())
                .totalMissionCount(puzzle.getPuzzleRegion().getTotalMissionCount())
                .collectedMissionCount(puzzle.getPuzzleRegion().getCollectedMissionCount())
                .puzzleCompleted(puzzle.getPuzzleRegion().isPuzzleCompleted())
                .build();
    }
}
