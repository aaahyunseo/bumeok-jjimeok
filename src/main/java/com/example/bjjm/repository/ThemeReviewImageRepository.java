package com.example.bjjm.repository;

import com.example.bjjm.entity.ThemeReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ThemeReviewImageRepository extends JpaRepository<ThemeReviewImage, UUID> {
}
