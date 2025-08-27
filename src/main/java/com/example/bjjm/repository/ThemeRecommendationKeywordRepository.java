package com.example.bjjm.repository;

import com.example.bjjm.entity.ThemeRecommendationKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ThemeRecommendationKeywordRepository extends JpaRepository<ThemeRecommendationKeyword, UUID> {
    List<ThemeRecommendationKeyword> findByKeyword(String keyword);
}
