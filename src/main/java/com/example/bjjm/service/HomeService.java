package com.example.bjjm.service;

import com.example.bjjm.dto.response.home.Top3PlaceData;
import com.example.bjjm.dto.response.home.Top3PlaceDto;
import com.example.bjjm.dto.response.home.UserProgressResponseDto;
import com.example.bjjm.entity.User;
import com.example.bjjm.entity.UserPuzzle;
import com.example.bjjm.repository.MissionRepository;
import com.example.bjjm.repository.ThemeReviewRepository;
import com.example.bjjm.repository.UserPuzzleRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final UserPuzzleRepository userPuzzleRepository;
    private final MissionRepository missionRepository;
    private final ThemeReviewRepository themeReviewRepository;

    /**
     * 퍼즐 수집 현황 (collectedCount/totalCount)
     * 성공 미션 개수 , 진행 테마 개수
     */
    public UserProgressResponseDto getUserProgress(User user) {
        List<UserPuzzle> userPuzzles = userPuzzleRepository.findAllByUser(user);

        int collectedPuzzleCount = 0;
        int completedMissionCount = 0;

        for (UserPuzzle userPuzzle : userPuzzles) {
            // 유저의 퍼즐 완료 여부
            if (userPuzzle.getPuzzleCompleted()) collectedPuzzleCount++;

            // 유저가 완료한 미션 수
            completedMissionCount += userPuzzle.getCollectedMissionCount();
        }

        // 전체 미션 수
        int totalMissionCount = (int) missionRepository.count();

        // 테마 완료 개수
        int completedThemeCount = themeReviewRepository.countByUser(user);

        return UserProgressResponseDto.builder()
                .userName(user.getName())
                .totalMissionCount(totalMissionCount)
                .collectedPuzzleCount(collectedPuzzleCount)
                .completedMissionCount(completedMissionCount)
                .completedThemeCount(completedThemeCount)
                .build();
    }

    /**
     * 인기맛집 Top-3 (가게 이름, 주소, 대표 이미지)
     */
    public Top3PlaceData getPopularRestaurantTop3() throws IOException{
        String url = "https://m.tripinfo.co.kr/trip_list.html?mode=food_rank&addr=busan";
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .get();

        List<Top3PlaceDto> top3 = new ArrayList<>();

        Elements rows = doc.select("table#list_table tr");

        for (int i = 0; i < Math.min(3, rows.size()); i++) {
            Element row = rows.get(i);

            // 가게 이름
            String name = row.selectFirst("div.title.b") != null
                    ? row.selectFirst("div.title.b").text()
                    : "";

            // 대표 이미지
            String imageUrl = "";
            Element img = row.selectFirst("td div img");
            if (img != null) {
                imageUrl = img.attr("src");
            }

            // 주소
            String address = "";
            Element desc = row.selectFirst("div.desc");
            if (desc != null) {
                address = desc.ownText().trim();
            }

            top3.add(Top3PlaceDto.builder()
                    .placeName(name)
                    .imageUrl(imageUrl)
                    .address(address)
                    .build());
        }

        return Top3PlaceData.from(top3);
    }


    /**
     * 추천 키워드 별 장소 추천 리스트 (이름, 주소, 대표 이미지)
     */
    public void getPlaceByKeyword(String keyword){

    }


    /**
     * 맞춤 테마 추천 5 (키워드 선택 : 여행 목적, 음식 취향, 감성 테마)
     */
    public void customizedTheme() {

    }

}
