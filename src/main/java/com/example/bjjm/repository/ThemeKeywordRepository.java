package com.example.bjjm.repository;

import com.example.bjjm.entity.ThemeKeyword;
import com.example.bjjm.entity.ThemeKeywordType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ThemeKeywordRepository extends JpaRepository<ThemeKeyword, UUID> {
    List<ThemeKeyword> findAllByKeyword(ThemeKeywordType keyword);
}
