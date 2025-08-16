package com.example.bjjm.repository;

import com.example.bjjm.entity.Theme;
import com.example.bjjm.entity.ThemeScrap;
import com.example.bjjm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ThemeScrapRepository extends JpaRepository<ThemeScrap, UUID> {
    boolean existsByUserAndTheme(User user, Theme theme);
    Optional<ThemeScrap> findByUserAndTheme(User user, Theme theme);
    List<ThemeScrap> findAllByUser(User user);
}
