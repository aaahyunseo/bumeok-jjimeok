package com.example.bjjm.service;

import com.example.bjjm.dto.request.theme.ThemeCreateRequestDto;
import com.example.bjjm.dto.request.theme.ThemeItemCreateDto;
import com.example.bjjm.dto.request.themeComment.ThemeCommentCreateDto;
import com.example.bjjm.dto.request.themeReview.ThemeReviewCreateDto;
import com.example.bjjm.dto.response.theme.ThemeDetailData;
import com.example.bjjm.dto.response.theme.ThemeListResponseData;
import com.example.bjjm.dto.response.themeComment.ThemeCommentListData;
import com.example.bjjm.dto.response.themeReview.ThemeReviewListData;
import com.example.bjjm.entity.*;
import com.example.bjjm.exception.NotFoundException;
import com.example.bjjm.exception.ValidationException;
import com.example.bjjm.exception.errorcode.ErrorCode;
import com.example.bjjm.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ThemeItemRepository themeItemRepository;
    private final ThemeKeywordRepository themeKeywordRepository;
    private final ThemeCommentRepository themeCommentRepository;
    private final ThemeReviewRepository themeReviewRepository;
    private final ThemeReviewImageRepository themeReviewImageRepository;


    /**
     * 테마 목록 전체 조회
     * **/
    @Transactional(readOnly = true)
    public ThemeListResponseData getThemeList(String themeType) {
        boolean isOfficial;

        if ("official".equals(themeType)) isOfficial = true;
        else if ("user".equals(themeType)) isOfficial = false;
        else throw new ValidationException(ErrorCode.OFFICIAL_OR_USER);

        List<Theme> themes = themeRepository.findByOfficial(isOfficial);

        return ThemeListResponseData.from(themes);
    }


    /**
     * 테마 키워드별 목록 조회
     * **/
    public ThemeListResponseData getThemeListByKeyword(String themeType, String keyword) {
        boolean isOfficial = "official".equals(themeType);
        ThemeKeywordType keywordType = ThemeKeywordType.valueOf(keyword.toUpperCase());

        List<Theme> themes = themeRepository.findByOfficialAndKeyword(isOfficial, keywordType);

        return ThemeListResponseData.from(themes);
    }


    /**
     * 테마 상세 보기
     * **/
    @Transactional
    public ThemeDetailData getThemeDetail(UUID themeId) {
        Theme theme = findThemeById(themeId);
        theme.increaseViewCount();
        return ThemeDetailData.from(theme);
    }


    /**
     * 테마 작성하기
     * **/
    @Transactional
    public void createTheme(User user, ThemeCreateRequestDto request) {
        System.out.println("========= User 정보 =========");
        System.out.println("ID: " + user.getId());
        System.out.println("Username: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("============================");

        // 1. 테마 생성
        Theme newTheme = Theme.builder()
                .title(request.getTitle())
                .introduction(request.getIntroduction())
                .user(user)
                .official(false)
                .viewCount(0L)
                .build();
        newTheme = themeRepository.save(newTheme);

        // 2. 키워드 추가
        if (request.getKeywords() != null) {
            Theme theme = newTheme;
            List<ThemeKeyword> themeKeywords = request.getKeywords().stream()
                    .map(k -> ThemeKeyword.builder()
                            .theme(theme)
                            .keyword(ThemeKeywordType.valueOf(k))
                            .build())
                    .collect(Collectors.toList());

            themeKeywordRepository.saveAll(themeKeywords);
        }

        // 3. 아이템 추가
        if (request.getItems() != null) {
            for (int i = 0; i < request.getItems().size(); i++) {
                ThemeItemCreateDto itemReq = request.getItems().get(i);

                ThemeItem themeItem = ThemeItem.builder()
                        .content(itemReq.getContent())
                        .address(itemReq.getAddress())
                        .theme(newTheme)
                        .build();

                // 이미지 추가
                if (itemReq.getImageUrls() != null && !itemReq.getImageUrls().isEmpty()) {
                    List<ThemeImage> images = itemReq.getImageUrls().stream()
                            .map(url -> ThemeImage.builder()
                                    .imageUrl(url)
                                    .themeItem(themeItem)
                                    .build())
                            .collect(Collectors.toList());

                    themeItem.setImageFiles(images);

                    // 대표 이미지 세팅
                    if (i == 0) {
                        newTheme.setMainImageUrl(images.get(0).getImageUrl());
                    }
                }

                themeItemRepository.save(themeItem);
            }
        }
    }


    /**
     * 테마 댓글 작성하기
     * **/
    public void createThemeComment(User user, UUID themeId, ThemeCommentCreateDto requestDto) {
        System.out.println(user.getName());

        Theme theme = findThemeById(themeId);

        ThemeComment newComment = ThemeComment.builder()
                .user(user)
                .theme(theme)
                .content(requestDto.getContent())
                .build();

        themeCommentRepository.save(newComment);
    }


    /**
     * 테마 댓글 조회
     * **/
    public ThemeCommentListData getThemeComments(UUID themeId) {
        Theme theme = findThemeById(themeId);
        List<ThemeComment> comments = themeCommentRepository.findAllByTheme(theme);
        return ThemeCommentListData.from(comments);
    }


    /**
     * 테마 리뷰 작성
     * **/
    @Transactional
    public void createThemeReview(User user, UUID themeId, ThemeReviewCreateDto requestDto) {
        Theme theme = findThemeById(themeId);

        ThemeReview newReview = ThemeReview.builder()
                .user(user)
                .theme(theme)
                .content(requestDto.getContent())
                .build();
        themeReviewRepository.save(newReview);

        if (requestDto.getImageUrls() != null && !requestDto.getImageUrls().isEmpty()) {
            List<ThemeReviewImage> images = requestDto.getImageUrls().stream()
                    .map(url -> ThemeReviewImage.builder()
                            .imageUrl(url)
                            .user(user)
                            .themeReview(newReview)
                            .build())
                    .collect(Collectors.toList());
            themeReviewImageRepository.saveAll(images);

            newReview.setImageFiles(images);
        }
    }


    /**
     * 테마 리뷰 조회
     * **/
    public ThemeReviewListData getThemeReviews(UUID themeId) {
        Theme theme = findThemeById(themeId);
        List<ThemeReview> reviews = themeReviewRepository.findAllByTheme(theme);
        return ThemeReviewListData.from(reviews);
    }


    public Theme findThemeById(UUID themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.THEME_NOT_FOUND));
    }
}
