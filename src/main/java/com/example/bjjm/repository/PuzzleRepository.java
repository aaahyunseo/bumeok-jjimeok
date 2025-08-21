package com.example.bjjm.repository;

import com.example.bjjm.entity.Puzzle;
import com.example.bjjm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PuzzleRepository extends JpaRepository<Puzzle, UUID> {
    List<Puzzle> findAllByUser(User user);
}
