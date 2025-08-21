package com.example.bjjm.repository;

import com.example.bjjm.entity.Mission;
import com.example.bjjm.entity.PuzzleRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MissionRepository extends JpaRepository<Mission, UUID> {
    List<Mission> findAllByPuzzleRegion (PuzzleRegion puzzleRegion);
}
