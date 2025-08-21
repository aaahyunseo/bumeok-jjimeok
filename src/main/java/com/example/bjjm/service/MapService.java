package com.example.bjjm.service;

import com.example.bjjm.dto.response.map.MenuDto;
import com.example.bjjm.dto.response.map.PlaceResponseDto;
import com.example.bjjm.exception.NotFoundException;
import com.example.bjjm.exception.errorcode.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MapService {

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public PlaceResponseDto getPlaceDetails(String query) {
        try {
            // 1. 네이버 검색 API 호출
            String url = "https://openapi.naver.com/v1/search/local.json?query=부산 " + query;
            System.out.println("url: " + url);

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", clientId);
            headers.set("X-Naver-Client-Secret", clientSecret);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            // 2. JSON 파싱
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response.getBody());
            JSONArray items = (JSONArray) json.get("items");

            if (items.isEmpty()) {
                throw new NotFoundException(ErrorCode.PLACE_INFO_NOT_FOUND);
            }

            JSONObject first = (JSONObject) items.get(0);
            String name = ((String) first.get("title")).replaceAll("<[^>]*>", "");
            String address = (String) first.get("address");
            String roadAddress = (String) first.get("roadAddress");
            String phone = (String) first.get("telephone");
            String link = (String) first.get("link");
            System.out.println("link: " + link);

            // 3. 상세 페이지 크롤링 (Jsoup)
            List<String> imageUrls = new ArrayList<>();
            String rating = null;
            List<MenuDto> menus = new ArrayList<>();

            try {
                Document doc = Jsoup.connect(link).get();

                // 전화번호
                Element phoneEl = doc.selectFirst("div.mqM2N.l8afP");
                if (phoneEl != null) {
                    phone = phoneEl.text();
                }

                // ✅ 이미지 크롤링 (최대 5개)
                doc.select("img").stream()
                        .limit(5)
                        .forEach(img -> imageUrls.add(img.attr("src")));

                // ✅ 별점 크롤링 (selector는 페이지 구조마다 다름 → 테스트 필요)
                rating = doc.select("span._3D6yR").text();

                // 메뉴 리스트 크롤링
                Elements menuElements = doc.select("li.E2jtL"); // 각 메뉴 아이템 li

                for (Element menuEl : menuElements) {
                    String menuName = menuEl.select("div.yQlqY span.lPzHi").text();   // 메뉴 이름
                    String menuPrice = menuEl.select("div.GXS1X em").text();         // 가격
                    String menuImage = menuEl.select("div.YBmM2 img.place_thumb").attr("src"); // 이미지

                    menus.add(MenuDto.builder()
                            .menuName(menuName)
                            .price(menuPrice)
                            .imageUrl(menuImage)
                            .build());
                }


            } catch (Exception e) {
                System.out.println("크롤링 실패: " + e.getMessage());
            }

            // 4. DTO 반환
            return PlaceResponseDto.builder()
                    .name(name)
                    .address(address)
                    .roadAddress(roadAddress)
                    .phone(phone)
                    .rating(rating)
                    .imageUrls(imageUrls)
                    .link(link)
                    .menus(menus)
                    .build();

        } catch (Exception e) {
            throw new NotFoundException(ErrorCode.PLACE_INFO_NOT_FOUND);
        }
    }
}

