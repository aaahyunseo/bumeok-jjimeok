package com.example.bjjm.repository;

import com.example.bjjm.entity.User;
import com.example.bjjm.entity.UserPuzzle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserPuzzleRepository extends JpaRepository<UserPuzzle, UUID> {
    List<UserPuzzle> findAllByUser(User user);
}
