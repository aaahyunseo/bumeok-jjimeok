package com.example.bjjm.repository;

import com.example.bjjm.entity.Mission;
import com.example.bjjm.entity.Puzzle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MissionRepository extends JpaRepository<Mission, UUID> {
    List<Mission> findAllByPuzzle (Puzzle puzzle);
    int countByPuzzle(Puzzle puzzle);
}
