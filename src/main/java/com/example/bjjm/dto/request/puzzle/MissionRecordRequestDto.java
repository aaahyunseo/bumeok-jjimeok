package com.example.bjjm.dto.request.puzzle;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MissionRecordRequestDto {
    private int score;
    private String content;
}
