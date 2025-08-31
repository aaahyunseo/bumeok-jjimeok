package com.example.bjjm.util;

import com.example.bjjm.entity.Mission;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelMissionLoader {

    public List<Mission> loadMissionsFromExcel(InputStream inputStream) throws IOException {
        List<Mission> missions = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) { // 첫 번째 행은 헤더라 가정
            Row row = sheet.getRow(i);
            if (row == null) continue;

            if (row.getCell(0) == null || row.getCell(0).getCellType() == CellType.BLANK) {
                continue;
            }

            Mission mission = new Mission();
            mission.setRegion(getCellValueAsString(row.getCell(0)));
            mission.setTitle(getCellValueAsString(row.getCell(1)));
            mission.setIntroduction(getCellValueAsString(row.getCell(2)));
            mission.setContent(normalizeNewlines(getCellValueAsString(row.getCell(3))));
            mission.setImageUrl(getCellValueAsString(row.getCell(4)));
            mission.setX(getCellValueAsString(row.getCell(5)));
            mission.setY(getCellValueAsString(row.getCell(6)));

            missions.add(mission);
        }
        workbook.close();
        return missions;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getDateCellValue().toString();
                } else {
                    double num = cell.getNumericCellValue();
                    if (num == Math.floor(num)) {
                        yield String.valueOf((long) num);
                    } else {
                        yield String.valueOf(num);
                    }
                }
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            case BLANK, _NONE, ERROR -> null;
        };
    }

    private String normalizeNewlines(String content) {
        if (content == null) return null;
        return content.replace("\\n", "\n");
    }
}


