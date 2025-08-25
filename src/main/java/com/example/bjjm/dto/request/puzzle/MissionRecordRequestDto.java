package com.example.bjjm.dto.request.puzzle;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MissionRecordRequestDto {

    private int score;

    private List<String> imageFiles;

    private String content;
}
