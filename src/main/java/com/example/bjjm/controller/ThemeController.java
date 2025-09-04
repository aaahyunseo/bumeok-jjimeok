package com.example.bjjm.controller;

import com.example.bjjm.authentication.AuthenticatedUser;
import com.example.bjjm.dto.ResponseDto;
import com.example.bjjm.dto.request.theme.ThemeCreateRequestDto;
import com.example.bjjm.dto.request.themeComment.ThemeCommentCreateDto;
import com.example.bjjm.dto.request.themeReview.ThemeReviewCreateDto;
import com.example.bjjm.dto.response.theme.ThemeDetailData;
import com.example.bjjm.dto.response.theme.ThemeListResponseData;
import com.example.bjjm.dto.response.themeComment.ThemeCommentListData;
import com.example.bjjm.dto.response.themeReview.ThemeReviewListData;
import com.example.bjjm.entity.User;
import com.example.bjjm.service.ThemeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    @Operation(summary = "테마 목록 전체 조회", description = "공식 또는 유저 테마 목록을 전체 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<ResponseDto<ThemeListResponseData>> getThemeList(@RequestParam("themeType") String themeType) {
        ThemeListResponseData themeListResponseData = themeService.getThemeList(themeType);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, themeType + " 테마 목록 전체 조회 완료", themeListResponseData), HttpStatus.OK);
    }

    @Operation(summary = "키워드별 테마 목록 전체 조회", description = "공식 테마의 키워드에 해당하는 목록을 전체 조회합니다.")
    @GetMapping("/keyword")
    public ResponseEntity<ResponseDto<ThemeListResponseData>> getThemeListByKeyword(@RequestParam("themeType") String themeType, @RequestParam("keyword") String keyword) {
        ThemeListResponseData themeListResponseData = themeService.getThemeListByKeyword(themeType, keyword);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, keyword + " 키워드 테마 목록 전체 조회 완료", themeListResponseData), HttpStatus.OK);
    }

    @Operation(summary = "테마 상세 조회", description = "테마 ID에 해당하는 글을 상세 조회합니다.")
    @GetMapping("/{themeId}/detail")
    public ResponseEntity<ResponseDto<ThemeDetailData>> getThemeDetail(@AuthenticatedUser User user, @PathVariable UUID themeId) {
        ThemeDetailData themeDetailData = themeService.getThemeDetail(user, themeId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "테마 상세 조회 성공", themeDetailData), HttpStatus.OK);
    }

    @Operation(summary = "테마 글 작성", description = "테마 글을 작성합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<Void>> createTheme(@AuthenticatedUser User user,
                                                         @Valid @ModelAttribute ThemeCreateRequestDto requestDto) throws IOException {
        themeService.createTheme(user, requestDto);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED, "테마 글 작성 성공"), HttpStatus.CREATED);
    }

    @Operation(summary = "테마 댓글 작성", description = "테마 댓글을 작성합니다.")
    @PostMapping("/{themeId}/comment")
    public ResponseEntity<ResponseDto<Void>> createThemeComment(@AuthenticatedUser User user,@PathVariable UUID themeId, @Valid @RequestBody ThemeCommentCreateDto requestDto) {
        themeService.createThemeComment(user, themeId, requestDto);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED, "테마 댓글 작성 성공"), HttpStatus.CREATED);
    }

    @Operation(summary = "테마 댓글 목록 조회", description = "테마 ID에 해당하는 댓글 목록을 조회합니다.")
    @GetMapping("/{themeId}/comment/list")
    public ResponseEntity<ResponseDto<ThemeCommentListData>> getThemeCommentList(@PathVariable UUID themeId) {
        ThemeCommentListData themeCommentListData = themeService.getThemeComments(themeId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "테마 댓글 목록 조회 성공", themeCommentListData), HttpStatus.OK);
    }

    @Operation(summary = "테마 리뷰 작성", description = "테마 ID에 해당하는 리뷰를 작성합니다.")
    @PostMapping("/{themeId}/review")
    public ResponseEntity<ResponseDto<Void>> createThemeReview(@AuthenticatedUser User user,
                                                               @PathVariable UUID themeId,
                                                               @ModelAttribute ThemeReviewCreateDto requestDto,
                                                               @RequestPart(value = "images", required = false) List<MultipartFile> images) throws IOException {
        themeService.createThemeReview(user, themeId, requestDto, images);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED, "테마 리뷰 작성 성공"), HttpStatus.CREATED);
    }

    @Operation(summary = "테마 리뷰 목록 조회", description = "테마 ID에 해당하는 리뷰 목록을 조회합니다.")
    @GetMapping("/{themeId}/review/list")
    public ResponseEntity<ResponseDto<ThemeReviewListData>> getThemeReviewList(@PathVariable UUID themeId) {
        ThemeReviewListData themeReviewListData = themeService.getThemeReviews(themeId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "테마 리뷰 목록 조회 성공", themeReviewListData), HttpStatus.OK);
    }

    @Operation(summary = "테마 스크랩 추가", description = "테마를 스크랩합니다.")
    @PostMapping("/{themeId}/scrap")
    public ResponseEntity<ResponseDto<Void>> createThemeScrap(@AuthenticatedUser User user, @PathVariable UUID themeId) {
        themeService.createThemeScrap(user, themeId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED, "테마 스크랩 추가 성공"), HttpStatus.CREATED);
    }

    @Operation(summary = "테마 스크랩 취소", description = "테마 스크랩을 취소합니다.")
    @DeleteMapping("/{themeId}/scrap")
    public ResponseEntity<ResponseDto<Void>> deleteThemeScrap(@AuthenticatedUser User user, @PathVariable UUID themeId) {
        themeService.deleteThemeScrap(user, themeId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "테마 스크랩 취소 성공"), HttpStatus.OK);
    }
}
