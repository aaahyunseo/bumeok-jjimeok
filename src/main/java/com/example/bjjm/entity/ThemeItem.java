package com.example.bjjm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "theme_items")
public class ThemeItem extends BaseEntity {
    @Column(nullable = false)
    private String content;

    @Column
    private String address;

    @OneToMany(mappedBy = "themeItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ThemeImage> imageFiles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id", nullable = false)
    private Theme theme;

    public void setImageFiles(List<ThemeImage> imageFiles) {
        this.imageFiles = imageFiles;
    }
}
