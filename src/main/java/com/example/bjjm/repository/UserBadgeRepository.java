package com.example.bjjm.repository;

import com.example.bjjm.entity.User;
import com.example.bjjm.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserBadgeRepository extends JpaRepository<UserBadge, UUID> {
    Optional<UserBadge> findByUserAndIsMainTrue(User user);
    List<UserBadge> findAllByUser(User user);
}
