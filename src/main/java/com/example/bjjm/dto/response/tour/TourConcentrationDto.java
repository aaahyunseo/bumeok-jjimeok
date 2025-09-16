package com.example.bjjm.dto.response.tour;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Getter
@Service
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourConcentrationDto {
    private String baseYmd;   // 기준연월일
    private String areaCd;    // 지역코드
    private String areaNm;    // 지역명
    private String signguCd;  // 시군구 코드
    private String signguNm;  // 시군구 명
    private String tAtsNm;    // 관광지명
    private double cnctrRate; // 집중률
}
