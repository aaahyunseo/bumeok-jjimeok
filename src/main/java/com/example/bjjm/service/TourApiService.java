package com.example.bjjm.service;

import com.example.bjjm.dto.response.tour.FestivalDto;
import com.example.bjjm.dto.response.tour.TourPlaceDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TourApiService {

    @Value("${tourapi.service-key}")
    private String serviceKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public List<TourPlaceDto> getNearbyTourList(double mapX, double mapY) throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString("https://apis.data.go.kr/B551011/KorService2/locationBasedList2")
                .queryParam("serviceKey", serviceKey)
                .queryParam("mapX", mapX)
                .queryParam("mapY", mapY)
                .queryParam("radius", 1000)
                .queryParam("arrange", "E")
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "BusanTripApp")
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
                .queryParam("MobileApp", "BusanTripApp")
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
}
