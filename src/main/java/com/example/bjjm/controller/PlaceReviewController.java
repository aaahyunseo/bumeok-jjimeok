package com.example.bjjm.controller;

import com.example.bjjm.authentication.AuthenticatedUser;
import com.example.bjjm.dto.ResponseDto;
import com.example.bjjm.dto.request.placeReview.PlaceReviewCreateRequestDto;
import com.example.bjjm.dto.response.placeReview.PlaceReviewListResponseData;
import com.example.bjjm.entity.User;
import com.example.bjjm.service.PlaceReviewService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place-reviews")
public class PlaceReviewController {

    private final PlaceReviewService placeReviewService;

    @Operation(summary = "장소 리뷰 작성하기", description = "장소 리뷰를 작성합니다.")
    @PostMapping
    public ResponseEntity<ResponseDto<Void>> createPlaceReview(@AuthenticatedUser User user,
                                                               @ModelAttribute PlaceReviewCreateRequestDto requestDto,
                                                               @RequestPart(value = "images", required = false) List<MultipartFile> images) throws IOException {
        placeReviewService.createPlaceReview(user, requestDto, images);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED, "장소 리뷰 작성 완료"), HttpStatus.CREATED);
    }

    @Operation(summary = "장소 리뷰 조회하기", description = "장소 리뷰 목록을 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<ResponseDto<PlaceReviewListResponseData>> getPlaceReviewList(@RequestParam("placeName") String placeName) {
        PlaceReviewListResponseData placeReviewListResponseData = placeReviewService.getPlaceReviewList(placeName);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, placeName +" 장소 리뷰 목록 조회 완료", placeReviewListResponseData), HttpStatus.OK);
    }
}
