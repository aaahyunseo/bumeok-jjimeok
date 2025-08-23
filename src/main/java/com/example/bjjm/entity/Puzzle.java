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
@Table(name = "puzzles")
public class Puzzle extends BaseEntity {

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private int totalMissionCount;

    @OneToMany(mappedBy = "puzzle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mission> missions;
}
