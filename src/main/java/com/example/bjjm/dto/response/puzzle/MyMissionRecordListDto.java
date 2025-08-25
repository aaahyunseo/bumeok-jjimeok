package com.example.bjjm.dto.response.puzzle;

import com.example.bjjm.entity.MissionRecord;
import com.example.bjjm.entity.MissionRecordImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyMissionRecordListDto {
    private String missionTitle;
    private String missionIntroduction;
    private List<String> imageUrls;
    private String content;
    private int score;
    private String createdAt;

    public static MyMissionRecordListDto from(MissionRecord missionRecord) {
        return MyMissionRecordListDto.builder()
                .missionTitle(missionRecord.getMission().getTitle())
                .missionIntroduction(missionRecord.getMission().getIntroduction())
                .imageUrls(missionRecord.getImageFiles().stream()
                        .map(MissionRecordImage::getImageUrl)
                        .collect(Collectors.toList()))
                .content(missionRecord.getContent())
                .score(missionRecord.getScore())
                .createdAt(missionRecord.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();
    }
}
