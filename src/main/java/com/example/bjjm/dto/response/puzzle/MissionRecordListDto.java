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
public class MissionRecordListDto {
    // 유저 이름
    private String userName;
    // 유저 프로필
    private String userProfile;
    // 등록일
    private String createdAt;
    // 미션 기록 사진 리스트
    private List<String> imageUrls;
    // 미션 후기 내용
    private String content;

    public static MissionRecordListDto from(MissionRecord missionRecord) {
        return MissionRecordListDto.builder()
                .userName(missionRecord.getUser().getName())
                .userProfile(missionRecord.getUser().getProfileImage())
                .createdAt(missionRecord.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .imageUrls(missionRecord.getImageFiles().stream()
                        .map(MissionRecordImage::getImageUrl)
                        .collect(Collectors.toList()))
                .content(missionRecord.getContent())
                .build();
    }
}
