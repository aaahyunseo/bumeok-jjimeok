package com.example.bjjm.repository;

import com.example.bjjm.entity.MissionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MissionRecordRepository extends JpaRepository<MissionRecord, UUID> {
}
