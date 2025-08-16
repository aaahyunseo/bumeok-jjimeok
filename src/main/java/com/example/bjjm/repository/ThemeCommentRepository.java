package com.example.bjjm.repository;

import com.example.bjjm.entity.Theme;
import com.example.bjjm.entity.ThemeComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ThemeCommentRepository extends JpaRepository<ThemeComment, UUID> {
    List<ThemeComment> findAllByTheme(Theme theme);
}
