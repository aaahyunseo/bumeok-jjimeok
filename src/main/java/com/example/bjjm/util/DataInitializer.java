package com.example.bjjm.util;

import com.example.bjjm.entity.Mission;
import com.example.bjjm.entity.Puzzle;
import com.example.bjjm.repository.MissionRepository;
import com.example.bjjm.repository.PuzzleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final PuzzleRepository puzzleRepository;
    private final MissionRepository missionRepository;
    private final ExcelPuzzleLoader excelPuzzleLoader;
    private final ExcelMissionLoader excelMissionLoader;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // 퍼즐 초기화
        if (puzzleRepository.count() == 0) {
            try (InputStream inputStream = getClass().getResourceAsStream("/data/puzzles.xlsx")) {
                List<Puzzle> puzzles = excelPuzzleLoader.loadPuzzlesFromExcel(inputStream);
                puzzleRepository.saveAll(puzzles);
                System.out.println(">>>>> 퍼즐 데이터 초기화 완료 (" + puzzles.size() + "건)");
            }
        } else {
            System.out.println(">>>>> 퍼즐 데이터가 이미 존재합니다. 초기화 건너뜀.");
        }

        // 미션 초기화
        if (missionRepository.count() == 0) {
            try (InputStream inputStream = getClass().getResourceAsStream("/data/missions.xlsx")) {
                List<Mission> missions = excelMissionLoader.loadMissionsFromExcel(inputStream);

                for (Mission mission : missions) {
                    Puzzle puzzle = puzzleRepository.findByRegion(mission.getRegion())
                            .orElseThrow(() -> new IllegalStateException(
                                    "해당 region의 Puzzle을 찾을 수 없음: " + mission.getRegion()
                            ));
                    mission.setPuzzle(puzzle);
                }

                missionRepository.saveAll(missions);
                System.out.println(">>>>> 미션 데이터 초기화 완료 (" + missions.size() + "건)");
            }
        } else {
            System.out.println(">>>>> 미션 데이터가 이미 존재합니다. 초기화 건너뜀.");
        }
    }
}


