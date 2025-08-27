package com.example.bjjm.dto.request.home;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KeywordListRequestDto {
    private List<String> selectedKeywords;
}
