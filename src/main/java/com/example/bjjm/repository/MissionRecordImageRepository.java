package com.example.bjjm.repository;

import com.example.bjjm.entity.MissionRecordImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MissionRecordImageRepository extends JpaRepository<MissionRecordImage, UUID> {
}
