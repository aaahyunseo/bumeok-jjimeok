package com.example.bjjm.dto.response.puzzle;

import com.example.bjjm.entity.UserPuzzle;
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

    public static PuzzleMapProgressDto from(UserPuzzle userPuzzle) {
        return PuzzleMapProgressDto.builder()
                .puzzleRegionId(userPuzzle.getPuzzle().getId())
                .puzzleRegion(userPuzzle.getPuzzle().getRegion())
                .totalMissionCount(userPuzzle.getPuzzle().getTotalMissionCount())
                .collectedMissionCount(userPuzzle.getCollectedMissionCount())
                .puzzleCompleted(userPuzzle.getPuzzleCompleted())
                .build();
    }
}
