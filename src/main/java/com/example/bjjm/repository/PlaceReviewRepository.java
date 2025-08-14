package com.example.bjjm.repository;

import com.example.bjjm.entity.PlaceReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlaceReviewRepository extends JpaRepository<PlaceReview, UUID> {
}
