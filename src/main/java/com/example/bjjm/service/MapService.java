package com.example.bjjm.service;

import com.example.bjjm.dto.response.map.MenuDto;
import com.example.bjjm.dto.response.map.PlaceResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MapService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getPlaceLink(String query) throws IOException {
        String searchUrl = "https://www.diningcode.com/list.dc?query=%EB%B6%80%EC%82%B0" + URLEncoder.encode(query, "UTF-8");
        Document doc = Jsoup.connect(searchUrl).userAgent("Mozilla/5.0").get();

        Element scriptTag = doc.select("script")
                .stream()
                .filter(el -> el.data().contains("listData"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("listData not found"));

        Pattern pattern = Pattern.compile("listData', '(.*)'\\)");
        Matcher matcher = pattern.matcher(scriptTag.data());
        if (!matcher.find()) throw new RuntimeException("listData JSON not found");

        String jsonStr = matcher.group(1).replace("\\\"", "\"");
        JsonNode root = objectMapper.readTree(jsonStr);

        JsonNode firstPlace = root.path("poi_section").path("list").get(0);
        String rid = firstPlace.path("v_rid").asText();

        return "https://www.diningcode.com/profile.php?rid=" + rid;
    }

    public PlaceResponseDto getPlaceDetailsFromLink(String query) throws IOException {

        String link = getPlaceLink(query);

        Document detailDoc = Jsoup.connect(link).userAgent("Mozilla/5.0").get();

        // 1. 가게 이름
        String name = detailDoc.select("h1.tit").text();

        // 2. 주소
        Elements addressElements = detailDoc.select("li.locat > a");
        String address = addressElements.stream()
                .map(Element::text)
                .collect(Collectors.joining(" "));

        // 3. 별점
        String rating = detailDoc.select("strong#lbl_review_point").text();

        // 4. 영업시간
        String hours = detailDoc.select("span.today-main-hours").text();

        // 5. 전화번호
        String phone = detailDoc.select("li.tel").text();

        // 6. 대표 사진
        List<String> imageUrls = detailDoc.select("img.scroll-to-photo-section.button")
                .eachAttr("src")
                .stream()
                .filter(url -> !url.isEmpty())
                .collect(Collectors.toList());

        // 7. 메뉴
        List<MenuDto> menus = new ArrayList<>();
        Elements menuItems = detailDoc.select("div.menu-info ul.list.Restaurant_MenuList li");
        for (Element li : menuItems) {
            String menuName = li.select("span.Restaurant_Menu").text();
            String menuPrice = li.select("p.r-txt.Restaurant_MenuPrice").text();
            if (!menuName.isEmpty() && !menuPrice.isEmpty()) {
                menus.add(new MenuDto(menuName, menuPrice));
            }
        }

        // DTO 생성
        PlaceResponseDto placeResponseDto = PlaceResponseDto.builder()
                .name(name)
                .address(address)
                .phone(phone)
                .rating(rating)
                .imageUrls(imageUrls)
                .menus(menus)
                .link(link)
                .build();
        return placeResponseDto;
    }
}

