package com.example.bjjm.service;

import com.example.bjjm.dto.response.map.PlaceResponseDto;
import com.example.bjjm.repository.PlaceReviewRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MapService {

    private final PlaceReviewRepository placeReviewRepository;

    private static final String SEARCH_URL = "https://m.tripinfo.co.kr/trip_list.html?mode=search&keyword=";

    public PlaceResponseDto getPlaceDetailsFromLink(String query) throws IOException {
        String url = SEARCH_URL + URLEncoder.encode(query, StandardCharsets.UTF_8);
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .get();

        Elements links = doc.select("div#list a");

        Element firstLink = links.stream()
                .filter(a -> a.text().replaceAll("\\s+", "").toLowerCase()
                        .contains(query.replaceAll("\\s+", "").toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(query + "에 해당하는 음식점 링크를 찾을 수 없습니다."));

        String detailUrl = firstLink.absUrl("href");

        Document detailDoc = Jsoup.connect(detailUrl)
                .userAgent("Mozilla/5.0")
                .get();

        Elements infoRows = detailDoc.select("div#info tr");

        Map<String, String> infoMap = new HashMap<>();
        for (Element row : infoRows) {
            Elements tds = row.select("td");
            if (tds.size() >= 2) {
                String key = tds.get(0).text().trim();
                String value = tds.get(1).text().trim();
                infoMap.put(key, value);
            }
        }

        String name = infoMap.getOrDefault("이름", "");
        String address = infoMap.getOrDefault("주소", "");
        String tel = infoMap.getOrDefault("문의 및 안내", "");
        String mainMenu = infoMap.getOrDefault("대표 메뉴", "");
        String otherMenu = infoMap.getOrDefault("기타 메뉴", "");
        String hours = infoMap.getOrDefault("영업시간", "");
        String holiday = infoMap.getOrDefault("쉬는날", "");

        Element detailInfoEl = detailDoc.selectFirst("div.gbg0.gbt1.gbb1.box");
        String detailInfo = detailInfoEl != null ? detailInfoEl.text() : "";

        Element thumbImg = detailDoc.selectFirst("img#thumb1");
        String mainImageUrl = thumbImg != null ? thumbImg.attr("src") : "";

        Object result = placeReviewRepository.findAvgScoreAndCount(name);

        double avgScore = 0.0;
        int reviewCount = 0;

        if (result != null) {
            Object[] arr = (Object[]) result;
            if (arr[0] != null) avgScore = ((Double) arr[0]);
            if (arr[1] != null) reviewCount = ((Long) arr[1]).intValue();
        }

        return PlaceResponseDto.builder()
                .placeName(name)
                .address(address)
                .tel(tel)
                .mainMenu(mainMenu)
                .otherMenu(otherMenu)
                .usageTime(hours)
                .holiday(holiday)
                .content(detailInfo)
                .mainImageUrl(mainImageUrl)
                .scoreAvg(avgScore)
                .reviewCount(reviewCount)
                .build();
    }
}
