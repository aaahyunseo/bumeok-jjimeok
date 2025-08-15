package com.example.bjjm.repository;

import com.example.bjjm.entity.ThemeKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ThemeKeywordRepository extends JpaRepository<ThemeKeyword, UUID> {
}
