package com.example.bjjm.util;

import com.example.bjjm.entity.Mission;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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

            Mission mission = new Mission();
            mission.setRegion(row.getCell(0).getStringCellValue());
            mission.setTitle(row.getCell(1).getStringCellValue());
            mission.setIntroduction(row.getCell(2).getStringCellValue());
            mission.setContent(row.getCell(3).getStringCellValue());
            mission.setImageUrl(row.getCell(4) != null ? row.getCell(4).getStringCellValue() : null);
            mission.setX(row.getCell(5).getStringCellValue());
            mission.setY(row.getCell(6).getStringCellValue());

            missions.add(mission);
        }
        workbook.close();
        return missions;
    }
}

