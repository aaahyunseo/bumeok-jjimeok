package com.example.bjjm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "user_puzzles")
public class UserPuzzle extends BaseEntity {
    @Column(nullable = false)
    private int collectedMissionCount;

    @Column(nullable = false)
    private Boolean puzzleCompleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "puzzle_id", nullable = false)
    private Puzzle puzzle;

    public void setCollectedMissionCount(int collectedMissionCount) {
        this.collectedMissionCount = collectedMissionCount;
    }

    public void setPuzzleCompleted(Boolean puzzleCompleted) {
        this.puzzleCompleted = puzzleCompleted;
    }
}
