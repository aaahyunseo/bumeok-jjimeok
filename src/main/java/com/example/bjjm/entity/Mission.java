package com.example.bjjm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "missions")
public class Mission extends BaseEntity {

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String introduction;

    @Column(nullable = false)
    private String content;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    private String x;

    @Column(nullable = false)
    private String y;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "puzzle_region_id", nullable = false)
    private PuzzleRegion puzzleRegion;

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MissionRecord> missionRecords;
}
