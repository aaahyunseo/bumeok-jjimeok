package com.example.bjjm.repository;

import com.example.bjjm.entity.ThemeImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ThemeImageRepository extends JpaRepository<ThemeImage, UUID> {
}
