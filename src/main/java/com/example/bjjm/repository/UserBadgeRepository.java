package com.example.bjjm.repository;

import com.example.bjjm.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserBadgeRepository extends JpaRepository<UserBadge, UUID> {
}
