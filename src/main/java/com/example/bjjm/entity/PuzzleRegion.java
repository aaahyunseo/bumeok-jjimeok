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
@Table(name = "puzzle_regions")
public class PuzzleRegion extends BaseEntity {

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private int totalMissionCount;

    @Column(nullable = false)
    private int collectedMissionCount;

    @Column(nullable = false)
    private boolean puzzleCompleted;

    @OneToMany(mappedBy = "puzzleRegion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mission> missions;
}
