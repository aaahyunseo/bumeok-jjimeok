package com.example.bjjm.controller;

import com.example.bjjm.authentication.AuthenticatedUser;
import com.example.bjjm.dto.ResponseDto;
import com.example.bjjm.dto.request.home.KeywordListRequestDto;
import com.example.bjjm.dto.response.home.CustomizedThemeData;
import com.example.bjjm.dto.response.home.RecommendThemeData;
import com.example.bjjm.dto.response.home.Top3PlaceData;
import com.example.bjjm.dto.response.home.UserProgressResponseDto;
import com.example.bjjm.entity.User;
import com.example.bjjm.service.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    private final HomeService homeService;

    @Operation(summary = "유저 진행현황 조회", description = "유저의 진행현황을 조회합니다.")
    @GetMapping
    public ResponseEntity<ResponseDto<UserProgressResponseDto>> getUserProgress(@AuthenticatedUser User user) {
        UserProgressResponseDto userProgress = homeService.getUserProgress(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "유저 진행현황 조회 완료", userProgress), HttpStatus.OK);
    }

    @Operation(summary = "부산 인기 맛집 top-3 조회", description = "부산 인기 맛집 top-3를 조회합니다.")
    @GetMapping("/top3")
    public ResponseEntity<ResponseDto<Top3PlaceData>> getPopularRestaurantTop3() throws IOException {
        Top3PlaceData top3PlaceData = homeService.getPopularRestaurantTop3();
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "부산 인기 맛집 top-3 조회 완료", top3PlaceData), HttpStatus.OK);
    }

    @Operation(summary = "키워드 별 테마 리스트 조회", description = "키워드 별 테마 리스트를 조회합니다.")
    @GetMapping("/theme")
    public ResponseEntity<ResponseDto<RecommendThemeData>> getThemeByKeyword(@RequestParam("keyword") String keyword) {
        RecommendThemeData recommendThemeData = homeService.getThemeByKeyword(keyword);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, keyword + " 키워드 테마 리스트 조회 완료", recommendThemeData), HttpStatus.OK);
    }

    @Operation(summary = "맞춤 테마 추천 5 조회", description = "맞춤 테마 추천 5개를 조회합니다.")
    @PostMapping("/theme")
    public ResponseEntity<ResponseDto<CustomizedThemeData>> getCustomizedThemeByKeywords(@RequestBody KeywordListRequestDto keywordListRequestDto) {
        CustomizedThemeData customizedThemeData = homeService.getCustomizedThemeByKeywords(keywordListRequestDto);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "맞춤 테마 추천 5 조회 완료", customizedThemeData), HttpStatus.OK);
    }
}
