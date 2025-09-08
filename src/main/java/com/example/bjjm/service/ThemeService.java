package com.example.bjjm.service;

import com.example.bjjm.dto.request.theme.ThemeCreateRequestDto;
import com.example.bjjm.dto.request.theme.ThemeItemCreateDto;
import com.example.bjjm.dto.request.themeComment.ThemeCommentCreateDto;
import com.example.bjjm.dto.request.themeReview.ThemeReviewCreateDto;
import com.example.bjjm.dto.response.theme.ThemeDetailData;
import com.example.bjjm.dto.response.theme.ThemeListResponseData;
import com.example.bjjm.dto.response.theme.ThemeListResponseDto;
import com.example.bjjm.dto.response.themeComment.ThemeCommentListData;
import com.example.bjjm.dto.response.themeReview.ThemeReviewListData;
import com.example.bjjm.entity.*;
import com.example.bjjm.exception.ConflictException;
import com.example.bjjm.exception.NotFoundException;
import com.example.bjjm.exception.ValidationException;
import com.example.bjjm.exception.errorcode.ErrorCode;
import com.example.bjjm.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ThemeItemRepository themeItemRepository;
    private final ThemeKeywordRepository themeKeywordRepository;
    private final ThemeCommentRepository themeCommentRepository;
    private final ThemeReviewRepository themeReviewRepository;
    private final ThemeScrapRepository themeScrapRepository;
    private final ThemeImageRepository themeImageRepository;

    private final ImageService imageService;
    private final S3Service s3Service;

    private LocalDate lastPickDate;
    private List<Theme> todayThemes;

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
     * 오늘의 추천 테마 조회
     * **/
    public ThemeListResponseData getTodayThemes() {
        LocalDate today = LocalDate.now();

        if (lastPickDate == null || !lastPickDate.equals(today)) {
            List<Theme> allThemes = themeRepository.findAll();
            if (allThemes.size() < 3) {
                throw new IllegalStateException("추천할 테마가 최소 3개 이상 등록되어야 합니다.");
            }

            Collections.shuffle(allThemes);
            todayThemes = allThemes.stream()
                    .limit(3)
                    .collect(Collectors.toList());

            todayThemes.sort(Comparator.comparing(Theme::getCreatedAt).reversed());
            lastPickDate = today;
        }

        return ThemeListResponseData.from(todayThemes);
    }


    /**
     * 테마 상세 보기
     * **/
    @Transactional
    public ThemeDetailData getThemeDetail(User user, UUID themeId) {
        Theme theme = findThemeById(themeId);
        theme.increaseViewCount();

        boolean scrapped = themeScrapRepository.existsByUserAndTheme(user, theme);

        return ThemeDetailData.from(theme, scrapped);
    }


    /**
     * 테마 작성하기
     * **/
    @Transactional
    public void createTheme(User user, ThemeCreateRequestDto request) throws IOException {
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
                    .toList();

            themeKeywordRepository.saveAll(themeKeywords);
        }

        // 3. 아이템 + 이미지 업로드
        if (request.getItems() != null) {
            Theme theme = newTheme;
            List<ThemeItem> themeItems = new ArrayList<>();

            for (ThemeItemCreateDto itemDto : request.getItems()) {
                String imageUrl = null;
                if (itemDto.getImageFile() != null && !itemDto.getImageFile().isEmpty()) {
                    imageUrl = s3Service.uploadThemeImage(itemDto.getImageFile());
                }

                ThemeItem item = ThemeItem.builder()
                        .content(itemDto.getContent())
                        .address(itemDto.getAddress())
                        .imageUrl(imageUrl)
                        .theme(newTheme)
                        .build();

                themeItems.add(item);
            }

            themeItemRepository.saveAll(themeItems);

            // 4. 대표 이미지 3장 저장
            List<String> itemImages = themeItems.stream()
                    .map(ThemeItem::getImageUrl)
                    .filter(Objects::nonNull)
                    .distinct()
                    .limit(3)
                    .toList();

            List<ThemeImage> mainImages = itemImages.stream()
                    .map(url -> ThemeImage.builder()
                            .imageUrl(url)
                            .theme(theme)
                            .build())
                    .toList();

            themeImageRepository.saveAll(mainImages);
            newTheme.setMainImageUrl(mainImages);
        }
    }


    /**
     * 테마 댓글 작성하기
     * **/
    public void createThemeComment(User user, UUID themeId, ThemeCommentCreateDto requestDto) {
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
    public void createThemeReview(User user, UUID themeId, ThemeReviewCreateDto requestDto, List<MultipartFile> images) throws IOException {
        Theme theme = findThemeById(themeId);

        ThemeReview newReview = ThemeReview.builder()
                .user(user)
                .theme(theme)
                .content(requestDto.getContent())
                .build();
        themeReviewRepository.save(newReview);

        // 리뷰 이미지 업로드
        if (images != null && !images.isEmpty()) {
            List<ThemeReviewImage> newImages = imageService.uploadThemeReviewImages(user, newReview, images);
            newReview.setImageFiles(newImages);
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

    /**
     * 테마 스크랩하기
     */
    @Transactional
    public void createThemeScrap(User user, UUID themeId) {
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.THEME_NOT_FOUND));

        boolean exists = themeScrapRepository.existsByUserAndTheme(user, theme);
        if (exists) {
            throw new ConflictException(ErrorCode.THEME_SCRAP_CONFLICT);
        }

        ThemeScrap scrap = ThemeScrap.builder()
                .user(user)
                .theme(theme)
                .build();

        themeScrapRepository.save(scrap);
    }

    /**
     * 테마 스크랩 취소하기
     */
    @Transactional
    public void deleteThemeScrap(User user, UUID themeId) {
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.THEME_NOT_FOUND));

        ThemeScrap scrap = themeScrapRepository.findByUserAndTheme(user, theme)
                .orElseThrow(() -> new NotFoundException(ErrorCode.THEME_SCRAP_NOT_FOUND));

        themeScrapRepository.delete(scrap);
    }


    public Theme findThemeById(UUID themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.THEME_NOT_FOUND));
    }
}
