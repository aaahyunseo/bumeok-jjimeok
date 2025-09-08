package com.example.bjjm.repository;

import com.example.bjjm.entity.Theme;
import com.example.bjjm.entity.ThemeReview;
import com.example.bjjm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ThemeReviewRepository extends JpaRepository<ThemeReview, UUID> {
    List<ThemeReview> findAllByTheme(Theme theme);
    int countByUser(User user);

    List<ThemeReview> findAllByUser(User user);
}
