package com.example.bjjm.controller;

import com.example.bjjm.dto.ResponseDto;
import com.example.bjjm.dto.response.map.PlaceResponseDto;
import com.example.bjjm.service.MapService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapController {

    private final MapService mapService;

    @Operation(summary = "장소 정보 조회하기", description = "장소 상세 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<ResponseDto<PlaceResponseDto>> getPlace(@RequestParam String query) throws IOException {
        PlaceResponseDto placeResponseDto = mapService.getPlaceDetails(query);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "장소 정보 조회 완료", placeResponseDto), HttpStatus.OK);
    }
}
