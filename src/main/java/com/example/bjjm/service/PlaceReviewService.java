package com.example.bjjm.service;

import com.example.bjjm.dto.request.placeReview.PlaceReviewCreateRequestDto;
import com.example.bjjm.dto.response.placeReview.PlaceReviewListResponseData;
import com.example.bjjm.entity.PlaceReview;
import com.example.bjjm.entity.PlaceReviewImage;
import com.example.bjjm.entity.User;
import com.example.bjjm.repository.PlaceReviewImageRepository;
import com.example.bjjm.repository.PlaceReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceReviewService {
    private final PlaceReviewRepository placeReviewRepository;
    private final PlaceReviewImageRepository placeReviewImageRepository;

    /**
     * 장소 리뷰 작성하기
     * **/
    public void createPlaceReview(User user, PlaceReviewCreateRequestDto requestDto) {
        PlaceReview newPlaceReview = PlaceReview.builder()
                .content(requestDto.getContent())
                .placeName(requestDto.getPlaceName())
                .address(requestDto.getAddress())
                .score(requestDto.getScore())
                .user(user)
                .build();
        placeReviewRepository.save(newPlaceReview);

        if (requestDto.getImageUrls() != null && !requestDto.getImageUrls().isEmpty()) {
            List<PlaceReviewImage> images = requestDto.getImageUrls().stream()
                    .map(url -> PlaceReviewImage.builder()
                            .imageUrl(url)
                            .user(user)
                            .placeReview(newPlaceReview)
                            .build())
                    .collect(Collectors.toList());
            placeReviewImageRepository.saveAll(images);

            newPlaceReview.setImageFiles(images);
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
