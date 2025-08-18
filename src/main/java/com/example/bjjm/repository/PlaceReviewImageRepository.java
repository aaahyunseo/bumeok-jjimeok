package com.example.bjjm.repository;

import com.example.bjjm.entity.PlaceReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlaceReviewImageRepository extends JpaRepository<PlaceReviewImage, UUID> {
}
