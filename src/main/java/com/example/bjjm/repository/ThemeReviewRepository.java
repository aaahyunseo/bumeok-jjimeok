package com.example.bjjm.repository;

import com.example.bjjm.entity.ThemeReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ThemeReviewRepository extends JpaRepository<ThemeReview, UUID> {
}
