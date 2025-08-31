package com.example.bjjm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "missions")
public class Mission extends BaseEntity {

    // 지역 이름
    @Column(nullable = false)
    private String region;

    // 미션 제목
    @Column(nullable = false)
    private String title;

    // 미션 한 줄 소개
    @Column(nullable = false)
    private String introduction;

    // 미션 내용
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // 미션 소개 사진(선택)
    @Column
    private String imageUrl;

    // 미션 위치 인증을 위한 장소 x좌표
    @Column(nullable = false)
    private String x;

    // 미션 위치 인증을 위한 장소 y좌표
    @Column(nullable = false)
    private String y;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "puzzle_id", nullable = false)
    private Puzzle puzzle;

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MissionRecord> missionRecords;
}
