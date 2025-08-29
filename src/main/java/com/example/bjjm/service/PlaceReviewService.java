package com.example.bjjm.service;

import com.example.bjjm.dto.request.placeReview.PlaceReviewCreateRequestDto;
import com.example.bjjm.dto.response.placeReview.PlaceReviewListResponseData;
import com.example.bjjm.entity.PlaceReview;
import com.example.bjjm.entity.PlaceReviewImage;
import com.example.bjjm.entity.User;
import com.example.bjjm.repository.PlaceReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceReviewService {
    private final PlaceReviewRepository placeReviewRepository;

    private final ImageService imageService;

    /**
     * 장소 리뷰 작성하기
     * **/
    public void createPlaceReview(User user, PlaceReviewCreateRequestDto requestDto, List<MultipartFile> images) throws IOException {
        PlaceReview newPlaceReview = PlaceReview.builder()
                .content(requestDto.getContent())
                .placeName(requestDto.getPlaceName())
                .address(requestDto.getAddress())
                .score(requestDto.getScore())
                .user(user)
                .build();
        placeReviewRepository.save(newPlaceReview);

        if (images != null && !images.isEmpty()) {
            List<PlaceReviewImage> newImages = imageService.uploadPlaceReviewImages(user, newPlaceReview, images);
            newPlaceReview.setImageFiles(newImages);
        }
    }

    /**
     * 장소 리뷰 목록 조회하기
     * **/
    public PlaceReviewListResponseData getPlaceReviewList(String placeName) {
        List<PlaceReview> placeReviews = placeReviewRepository.findAllByPlaceName(placeName);
        return PlaceReviewListResponseData.from(placeReviews);
    }
}
