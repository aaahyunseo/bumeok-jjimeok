package com.example.bjjm.repository;

import com.example.bjjm.entity.Theme;
import com.example.bjjm.entity.ThemeKeywordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ThemeRepository extends JpaRepository<Theme, UUID> {

    List<Theme> findByOfficial(boolean official);

    @Query("SELECT DISTINCT t FROM Theme t " +
            "JOIN t.keywords tk " +
            "WHERE t.official = :official " +
            "AND tk.keyword = :keyword")
    List<Theme> findByOfficialAndKeyword(@Param("official") boolean official,
                                         @Param("keyword") ThemeKeywordType keyword);
}
