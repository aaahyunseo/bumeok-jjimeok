package com.example.bjjm.repository;

import com.example.bjjm.entity.PuzzleRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PuzzleRegionRepository extends JpaRepository<PuzzleRegion, UUID> {
}
