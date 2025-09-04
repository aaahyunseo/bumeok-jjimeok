package com.example.bjjm.controller;

import com.example.bjjm.authentication.AuthenticatedUser;
import com.example.bjjm.dto.ResponseDto;
import com.example.bjjm.dto.response.theme.ThemeListResponseData;
import com.example.bjjm.dto.response.user.UserInfoResponseDto;
import com.example.bjjm.entity.User;
import com.example.bjjm.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저 정보 조회", description = "유저 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<ResponseDto<UserInfoResponseDto>> getUserInfo(@AuthenticatedUser User user) {
        UserInfoResponseDto userInfoResponseDto = userService.getUserInfo(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "유저 정보 조회 완료", userInfoResponseDto), HttpStatus.OK);
    }

    @Operation(summary = "유저의 테마 스크랩 목록 조회", description = "유저가 스크랩한 테마 목록을 조회합니다.")
    @GetMapping("/scrap")
    public ResponseEntity<ResponseDto<ThemeListResponseData>> getUserThemeScrap(@AuthenticatedUser User user) {
        ThemeListResponseData scrapThemeList = userService.getUserThemeScrap(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "유저의 테마 스크랩 목록 조회 완료", scrapThemeList), HttpStatus.OK);
    }
}
