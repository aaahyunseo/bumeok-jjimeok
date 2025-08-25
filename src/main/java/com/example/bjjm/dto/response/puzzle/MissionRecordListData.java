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
public class MissionRecordListData {
    private List<MissionRecordListDto> missionRecordList;

    public static MissionRecordListData from(List<MissionRecord> missionRecordList) {
        List<MissionRecordListDto> dtos = missionRecordList.stream()
                .map(MissionRecordListDto::from)
                .collect(Collectors.toList());

        return MissionRecordListData.builder()
                .missionRecordList(dtos)
                .build();
    }
}
