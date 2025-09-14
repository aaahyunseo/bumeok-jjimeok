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

    @Query("SELECT u, COUNT(mr) " +
            "FROM User u " +
            "LEFT JOIN MissionRecord mr ON mr.user = u " +
            "GROUP BY u " +
            "ORDER BY COUNT(mr) DESC, u.name ASC")
    List<Object[]> findUserMissionSuccessCounts();
}
