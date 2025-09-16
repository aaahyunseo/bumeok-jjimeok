package com.example.bjjm.service;

import com.example.bjjm.dto.response.tour.*;
import com.example.bjjm.exception.NotFoundException;
import com.example.bjjm.exception.errorcode.ErrorCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TourApiService {

    @Value("${tourapi.service-key}")
    private String serviceKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final XmlMapper xmlMapper = new XmlMapper();

    public List<TourPlaceDto> getNearbyTourList(double mapX, double mapY) throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString("https://apis.data.go.kr/B551011/KorService2/locationBasedList2")
                .queryParam("serviceKey", serviceKey)
                .queryParam("mapX", mapX)
                .queryParam("mapY", mapY)
                .queryParam("radius", 1000)
                .queryParam("arrange", "E")
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "Bumeok-jjikmeok")
                .queryParam("numOfRows", 20)
                .queryParam("pageNo", 1)
                .queryParam("_type", "json")
                .build(true)
                .toUri();

        String response = restTemplate.getForObject(uri, String.class);
        JsonNode items = objectMapper.readTree(response)
                .path("response").path("body").path("items").path("item");

        List<TourPlaceDto> list = new ArrayList<>();
        if (items.isArray()) {
            for (JsonNode node : items) {
                TourPlaceDto dto = TourPlaceDto.builder()
                        .address(node.path("addr1").asText())
                        .mapX(node.path("mapx").asText())
                        .mapY(node.path("mapy").asText())
                        .dist(node.path("dist").asText())
                        .firstImage(node.path("firstimage").asText())
                        .tel(node.path("tel").asText())
                        .title(node.path("title").asText())
                        .build();
                list.add(dto);
            }
        }
        return list;
    }


    public List<FestivalDto> getFestivalList(String year, String code) throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString("https://apis.data.go.kr/B551011/KorService2/searchFestival2")
                .queryParam("serviceKey", serviceKey)
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "Bumeok-jjikmeok")
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 100)
                .queryParam("eventStartDate", year + "0101")
                .queryParam("eventEndDate", year + "1231")
                .queryParam("arrange", "C")
                .queryParam("_type", "json")
                .queryParam("areaCode", "6")
                .queryParam("sigunguCode", code)
                .build(true)
                .toUri();

        String response = restTemplate.getForObject(uri, String.class);
        JsonNode items = objectMapper.readTree(response)
                .path("response").path("body").path("items").path("item");

        List<FestivalDto> list = new ArrayList<>();
        if (items.isArray()) {
            for (JsonNode node : items) {
                FestivalDto dto = FestivalDto.builder()
                        .address(node.path("addr1").asText())
                        .mapX(node.path("mapx").asText())
                        .mapY(node.path("mapy").asText())
                        .firstImage(node.path("firstimage").asText())
                        .tel(node.path("tel").asText())
                        .title(node.path("title").asText())
                        .eventStartDate(node.path("eventstartdate").asText())
                        .eventEndDate(node.path("eventenddate").asText())
                        .build();
                list.add(dto);
            }
        }
        return list;
    }

    public List<FoodPlaceDto> getFoodPlaceList(int pageNo, int numOfRows) throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString("https://apis.data.go.kr/6260000/FoodService/getFoodKr")
                .queryParam("serviceKey", serviceKey)
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageNo", pageNo)
                .build(true)
                .toUri();

        String response = restTemplate.getForObject(uri, String.class);

        JsonNode items = xmlMapper.readTree(response)
                .path("body").path("items").path("item");

        List<FoodPlaceDto> list = new ArrayList<>();
        if (items.isArray()) {
            for (JsonNode node : items) {
                FoodPlaceDto dto = FoodPlaceDto.builder()
                        .title(node.path("TITLE").asText())
                        .place(node.path("PLACE").asText())
                        .mapX(node.path("LNG").asDouble())
                        .mapY(node.path("LAT").asDouble())
                        .address(node.path("ADDR1").asText())
                        .tel(node.path("CNTCT_TEL").asText())
                        .homepageUrl(node.path("HOMEPAGE_URL").asText())
                        .usageTime(node.path("USAGE_DAY_WEEK_AND_TIME").asText())
                        .mainMenu(node.path("RPRSNTV_MENU").asText())
                        .mainImage(node.path("MAIN_IMG_NORMAL").asText())
                        .content(node.path("ITEMCNTNTS").asText())
                        .build();
                list.add(dto);
            }
        }
        return list;
    }

    public FoodPlaceDto getFoodPlace(String placeName) throws Exception {
        List<FoodPlaceDto> allPlaces = getFoodPlaceList(1, 500);

        return allPlaces.stream()
                .filter(dto -> {
                    String dataPlace = dto.getPlace();
                    if (dataPlace == null) return false;
                    return dataPlace.replaceAll("\\s+", "")
                            .toLowerCase()
                            .contains(placeName.replaceAll("\\s+", "").toLowerCase());
                })
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ErrorCode.PLACE_INFO_NOT_FOUND));
    }

    // 부산 관광지 방문자 집중률 추이 조회
    public List<TourConcentrationDto> getTourConcentrationList(String signguNm, String tAtsNm) throws Exception {
        String signguCd = SIGNGU_CODE_MAP.get(signguNm);

        if (signguCd == null) {
            throw new IllegalArgumentException("존재하지 않는 시군구명입니다: " + signguNm);
        }

        URI uri = UriComponentsBuilder
                .fromUriString("http://apis.data.go.kr/B551011/TatsCnctrRateService/tatsCnctrRatedList")
                .queryParam("serviceKey", serviceKey)
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "Bumeok-jjikmeok")
                .queryParam("_type", "json")
                .queryParam("numOfRows", 30)
                .queryParam("pageNo", 1)
                .queryParam("areaCd", 26)
                .queryParam("signguCd", signguCd)
                .queryParam("tAtsNm", URLEncoder.encode(tAtsNm, StandardCharsets.UTF_8))
                .build(true)
                .toUri();

        String response = restTemplate.getForObject(uri, String.class);

        JsonNode items = objectMapper.readTree(response)
                .path("response").path("body").path("items").path("item");

        List<TourConcentrationDto> list = new ArrayList<>();
        if (items.isArray()) {
            for (JsonNode node : items) {
                TourConcentrationDto dto = TourConcentrationDto.builder()
                        .baseYmd(node.path("baseYmd").asText())
                        .areaCd(node.path("areaCd").asText())
                        .areaNm(node.path("areaNm").asText())
                        .signguCd(node.path("signguCd").asText())
                        .signguNm(node.path("signguNm").asText())
                        .tAtsNm(node.path("tAtsNm").asText())
                        .cnctrRate(node.path("cnctrRate").asDouble())
                        .build();
                list.add(dto);
            }
        }
        return list;
    }

    // 30일 간 cnctrRate 평균 계산
    public TourConcentrationAverageDto getTourConcentrationAverage(String signguNm, String tAtsNm) throws Exception {
        List<TourConcentrationDto> list = getTourConcentrationList(signguNm, tAtsNm);

        if (list.isEmpty()) {
            throw new NotFoundException(ErrorCode.PLACE_INFO_NOT_FOUND);
        }

        double avgRate = list.stream()
                .mapToDouble(TourConcentrationDto::getCnctrRate)
                .average()
                .orElse(0.0);

        TourConcentrationDto first = list.get(0);

        return TourConcentrationAverageDto.builder()
                .areaCd(first.getAreaCd())
                .areaNm(first.getAreaNm())
                .signguCd(first.getSignguCd())
                .signguNm(first.getSignguNm())
                .tAtsNm(first.getTAtsNm())
                .avgCnctrRate(avgRate)
                .build();
    }

    // 시군구 코드 매핑
    private static final Map<String, String> SIGNGU_CODE_MAP = Map.ofEntries(
            Map.entry("중구", "26110"),
            Map.entry("서구", "26140"),
            Map.entry("동구", "26170"),
            Map.entry("영도구", "26200"),
            Map.entry("부산진구", "26230"),
            Map.entry("동래구", "26260"),
            Map.entry("남구", "26290"),
            Map.entry("북구", "26320"),
            Map.entry("해운대구", "26350"),
            Map.entry("사하구", "26380"),
            Map.entry("금정구", "26410"),
            Map.entry("강서구", "26440"),
            Map.entry("연제구", "26470"),
            Map.entry("수영구", "26500"),
            Map.entry("사상구", "26530"),
            Map.entry("기장군", "26710")
    );

    // 관광지 제목으로 상세 사진 목록 조회
    public List<TourPlacePhotoDto> getGalleryDetail(String title) {
        try {
            URI uri = UriComponentsBuilder
                    .fromHttpUrl("http://apis.data.go.kr/B551011/PhotoGalleryService1/galleryDetailList1")
                    .queryParam("serviceKey", serviceKey)
                    .queryParam("MobileOS", "ETC")
                    .queryParam("MobileApp", "Bumeok-jjikmeok")
                    .queryParam("_type", "json")
                    .queryParam("title", URLEncoder.encode(title, StandardCharsets.UTF_8))
                    .queryParam("numOfRows", 10)
                    .queryParam("pageNo", 1)
                    .build(true)
                    .toUri();

            String response = restTemplate.getForObject(uri, String.class);

            JsonNode root = objectMapper.readTree(response);
            JsonNode items = root.path("response").path("body").path("items").path("item");

            List<TourPlacePhotoDto> result = new ArrayList<>();
            if (items.isArray()) {
                for (JsonNode item : items) {
                    TourPlacePhotoDto dto = new TourPlacePhotoDto(
                            item.path("galContentId").asText(),
                            item.path("galTitle").asText(),
                            item.path("galWebImageUrl").asText(),
                            item.path("galPhotographyMonth").asText(),
                            item.path("galPhotographyLocation").asText(),
                            item.path("galPhotographer").asText(),
                            item.path("galSearchKeyword").asText()
                    );
                    result.add(dto);
                }
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException("관광사진 상세 조회 중 오류 발생", e);
        }
    }

}
