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
public class TourConcentrationAverageDto {
    private String areaCd;
    private String areaNm;
    private String signguCd;
    private String signguNm;
    private String tAtsNm;
    private double avgCnctrRate;
}
