package com.example.bjjm.controller;

import com.example.bjjm.dto.ResponseDto;
import com.example.bjjm.dto.response.tour.*;
import com.example.bjjm.service.TourApiService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tour")
public class TourApiController {

    private final TourApiService tourApiService;

    @Operation(summary = "내 주변 관광지 조회", description = "좌표(x,y) 기준 반경 1000m 관광지 목록을 거리순으로 조회합니다.")
    @GetMapping
    public ResponseEntity<ResponseDto<List<TourPlaceDto>>> getListOfTourist(@RequestParam double x, @RequestParam double y) throws Exception {
        List<TourPlaceDto> tourPlaceDtoList = tourApiService.getNearbyTourList(x, y);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "내 주변 관광지 조회 완료", tourPlaceDtoList), HttpStatus.OK);
    }

    @Operation(summary = "부산 행사/공연/축제 조회", description = "시작일자와 종료일자를 입력하면 부산 내 행사/공연/축제 정보를 반환합니다.")
    @GetMapping("/festival")
    public ResponseEntity<ResponseDto<List<FestivalDto>>> getFestivalList(@RequestParam String year, @RequestParam String code) throws Exception {
        List<FestivalDto> festivalList = tourApiService.getFestivalList(year, code);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "부산 행사/공연/축제 조회 완료", festivalList), HttpStatus.OK);
    }

    @Operation(summary = "부산 가게 상세 정보 조회", description = "부산 가게 상세 정보를 조회합니다.")
    @GetMapping("/place")
    public ResponseEntity<ResponseDto<FoodPlaceDto>> getFoodPlaceList(@RequestParam String placeName) throws Exception {
        FoodPlaceDto foodPlaceDto = tourApiService.getFoodPlace(placeName);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "부산 가게 상세 정보 조회 완료", foodPlaceDto), HttpStatus.OK);
    }

    @Operation(summary = "부산 관광지별 집중률 추이 평균 조회", description = "부산 관광지별 집중률 추이 평균을 조회합니다.")
    @GetMapping("/concentration")
    public ResponseEntity<ResponseDto<TourConcentrationAverageDto>> getTourConcentration(@RequestParam String signguNm, @RequestParam String tAtsNm) throws Exception {
        TourConcentrationAverageDto tourConcentrationAverage = tourApiService.getTourConcentrationAverage(signguNm, tAtsNm);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, tAtsNm + " 관광지 집중률 추이 평균 조회 완료", tourConcentrationAverage), HttpStatus.OK);
    }
}
