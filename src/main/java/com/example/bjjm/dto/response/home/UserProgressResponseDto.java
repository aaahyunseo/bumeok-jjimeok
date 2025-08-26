package com.example.bjjm.dto.response.home;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProgressResponseDto {
    private String userName;
    private int totalMissionCount;
    private int collectedPuzzleCount;
    private int completedMissionCount;
    private int completedThemeCount;
}
