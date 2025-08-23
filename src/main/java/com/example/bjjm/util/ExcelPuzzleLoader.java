package com.example.bjjm.util;

import com.example.bjjm.entity.Puzzle;
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
public class ExcelPuzzleLoader {

    public List<Puzzle> loadPuzzlesFromExcel(InputStream inputStream) throws IOException {
        List<Puzzle> puzzles = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) { // 첫 줄은 헤더라 가정
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Puzzle puzzle = new Puzzle();
            puzzle.setRegion(row.getCell(0).getStringCellValue());
            puzzle.setTotalMissionCount((int) row.getCell(1).getNumericCellValue());

            puzzles.add(puzzle);
        }

        workbook.close();
        return puzzles;
    }
}

