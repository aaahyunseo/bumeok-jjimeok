package com.example.bjjm.repository;

import com.example.bjjm.entity.Mission;
import com.example.bjjm.entity.MissionRecord;
import com.example.bjjm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MissionRecordRepository extends JpaRepository<MissionRecord, UUID> {
    List<MissionRecord> findAllByMission (Mission mission);
    List<MissionRecord> findAllByUser (User user);
    boolean existsByMissionAndUser (Mission mission, User user);

    @Query("SELECT mr.user AS user, COUNT(mr) AS successCount " +
            "FROM MissionRecord mr " +
            "GROUP BY mr.user " +
            "ORDER BY successCount DESC")
    List<Object[]> findUserMissionSuccessCounts();
}
