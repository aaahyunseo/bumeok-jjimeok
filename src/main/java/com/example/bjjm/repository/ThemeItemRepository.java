package com.example.bjjm.repository;

import com.example.bjjm.entity.ThemeItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ThemeItemRepository extends JpaRepository<ThemeItem, UUID> {
}
