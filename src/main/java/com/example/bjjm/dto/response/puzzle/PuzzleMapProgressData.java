package com.example.bjjm.dto.response.puzzle;

import com.example.bjjm.entity.Puzzle;
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
public class PuzzleMapProgressData {
    private List<PuzzleMapProgressDto> puzzleMapProgressDtos;

    public static PuzzleMapProgressData from(List<Puzzle> puzzles) {
        List<PuzzleMapProgressDto> dtos = puzzles.stream()
                .map(PuzzleMapProgressDto::from)
                .collect(Collectors.toList());

        return PuzzleMapProgressData.builder()
                .puzzleMapProgressDtos(dtos)
                .build();
    }
}
