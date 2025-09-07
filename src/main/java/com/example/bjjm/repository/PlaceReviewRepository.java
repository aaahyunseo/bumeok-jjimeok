package com.example.bjjm.repository;

import com.example.bjjm.entity.PlaceReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PlaceReviewRepository extends JpaRepository<PlaceReview, UUID> {
    List<PlaceReview> findAllByPlaceName(String placeName);
    @Query("SELECT AVG(r.score), COUNT(r) FROM PlaceReview r WHERE r.placeName = :placeName")
    Object findAvgScoreAndCount(@Param("placeName") String placeName);
}
