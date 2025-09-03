package com.example.bjjm.util;

import com.example.bjjm.entity.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
public class ExcelThemeLoader {

    private final DataFormatter formatter = new DataFormatter();

    public List<Theme> loadThemesFromExcel(InputStream inputStream) throws IOException {
        Map<String, Theme> themeByTitle = new LinkedHashMap<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String title = getCellValue(row, 0);
                String introduction = getCellValue(row, 1);
                if (title.isBlank() && introduction.isBlank()) continue;

                Theme theme = themeByTitle.computeIfAbsent(title, t ->
                        Theme.builder()
                                .title(t)
                                .introduction(introduction)
                                .official(true)
                                .viewCount(0L)
                                .themeItems(new ArrayList<>())
                                .keywords(new ArrayList<>())
                                .mainImagesUrls(new ArrayList<>())
                                .build()
                );

                // 키워드
                String keywordsCell = getCellValue(row, 2);
                if (!keywordsCell.isBlank()) {
                    for (String raw : keywordsCell.split(",")) {
                        String k = raw.trim();
                        if (k.isEmpty()) continue;
                        try {
                            ThemeKeywordType type = ThemeKeywordType.valueOf(k.toUpperCase());
                            boolean exists = theme.getKeywords().stream()
                                    .anyMatch(kw -> kw.getKeyword() == type);
                            if (!exists) {
                                theme.getKeywords().add(
                                        ThemeKeyword.builder()
                                                .keyword(type)
                                                .theme(theme)
                                                .build()
                                );
                            }
                        } catch (IllegalArgumentException e) {
                            System.out.println("[keywords] 잘못된 키워드 값: " + k + " (row=" + i + ")");
                        }
                    }
                }

                // 아이템
                for (int c = 3; c < row.getLastCellNum(); c += 3) {
                    String itemContent  = getCellValue(row, c);
                    String itemAddress  = getCellValue(row, c + 1);
                    String itemImageUrl = getCellValue(row, c + 2);

                    if (itemContent.isBlank() && itemAddress.isBlank() && itemImageUrl.isBlank()) continue;

                    ThemeItem item = ThemeItem.builder()
                            .content(itemContent)
                            .address(itemAddress)
                            .theme(theme)
                            .imageUrl(itemImageUrl)
                            .build();

                    theme.getThemeItems().add(item);
                }
            }
        }

        // 각 테마 별 대표 이미지 선택
        for (Theme theme : themeByTitle.values()) {
            List<String> allImageUrls = theme.getThemeItems().stream()
                    .map(ThemeItem::getImageUrl)
                    .filter(url -> url != null && !url.isBlank())
                    .toList();

            List<String> selected = allImageUrls.stream()
                    .limit(3)
                    .toList();

            for (String url : selected) {
                theme.getMainImagesUrls().add(
                        ThemeImage.builder()
                                .imageUrl(url)
                                .theme(theme)
                                .build()
                );
            }
        }


        return new ArrayList<>(themeByTitle.values());
    }

    private String getCellValue(Row row, int cellIndex) {
        if (cellIndex < 0 || cellIndex >= row.getLastCellNum()) return "";
        Cell cell = row.getCell(cellIndex);
        if (cell == null) return "";
        return formatter.formatCellValue(cell).trim();
    }
}
