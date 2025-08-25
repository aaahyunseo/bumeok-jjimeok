package com.example.bjjm.repository;

import com.example.bjjm.entity.PlaceReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlaceReviewRepository extends JpaRepository<PlaceReview, UUID> {
    List<PlaceReview> findAllByPlaceName(String placeName);
}
