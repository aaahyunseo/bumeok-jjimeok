package com.example.bjjm.repository;

import com.example.bjjm.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MissionRepository extends JpaRepository<Mission, UUID> {
}
