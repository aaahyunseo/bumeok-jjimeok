package com.example.bjjm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ThemeKeywordType {
    LOCAL_TOUR("지역 탐방"),
    FOOD_TOUR("음식 투어"),
    ALLEY_TRIP("골목 여행"),
    DATE_COURSE("데이트 코스"),
    SOLO_TRIP("혼행 감성"),
    FAMILY_WITH_CHILD("가족/아이와"),
    NIGHT_VIEW("야경/야식"),
    CAFE_PHOTO("카페&사진"),
    MOVIE_LOCATION("영화/드라마 장소"),
    LOCAL_COURSE("현지인 코스");

    private final String displayName;
}

