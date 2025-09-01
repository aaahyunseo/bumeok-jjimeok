package com.example.bjjm.service;

import com.example.bjjm.dto.response.map.PlaceResponseDto;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Service
@RequiredArgsConstructor
public class MapService {

    private static final String SEARCH_URL = "https://m.tripinfo.co.kr/trip_list.html?mode=search&keyword=";

    /**
     * 검색어로 장소를 조회하고 상세 정보를 크롤링
     */
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

        // 상세 페이지 접속
        Document detailDoc = Jsoup.connect(detailUrl)
                .userAgent("Mozilla/5.0")
                .get();

        Elements infoRows = detailDoc.select("div#info tr");

        String name = infoRows.size() > 0 ? infoRows.get(0).select("td").get(1).text() : "";
        String address = infoRows.size() > 2 ? infoRows.get(2).select("td").get(1).text() : "";
        String tel = infoRows.size() > 3 ? infoRows.get(3).select("td").get(1).text() : "";
        String mainMenu = infoRows.size() > 4 ? infoRows.get(4).select("td").get(1).text() : "";
        String otherMenu = infoRows.size() > 5 ? infoRows.get(5).select("td").get(1).text() : "";
        String hours = infoRows.size() > 8 ? infoRows.get(8).select("td").get(1).text() : "";
        String holiday = infoRows.size() > 9 ? infoRows.get(9).select("td").get(1).text() : "";

        Element detailInfoEl = detailDoc.selectFirst("div.gbg0.gbt1.gbb1.box");
        String detailInfo = detailInfoEl != null ? detailInfoEl.text() : "";

        return PlaceResponseDto.builder()
                .placeName(name)
                .address(address)
                .tel(tel)
                .mainMenu(mainMenu)
                .otherMenu(otherMenu)
                .usageTime(hours)
                .holiday(holiday)
                .content(detailInfo)
                .build();
    }
}

