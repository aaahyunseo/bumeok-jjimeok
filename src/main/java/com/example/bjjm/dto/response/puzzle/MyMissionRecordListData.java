package com.example.bjjm.dto.response.puzzle;

import com.example.bjjm.entity.MissionRecord;
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
public class MyMissionRecordListData {
    private List<MyMissionRecordListDto> missionRecords;

    public static MyMissionRecordListData from(List<MissionRecord> missionRecords) {
        List<MyMissionRecordListDto> dtos = missionRecords.stream()
                .map(MyMissionRecordListDto::from)
                .collect(Collectors.toList());

        return MyMissionRecordListData.builder()
                .missionRecords(dtos)
                .build();
    }
}
