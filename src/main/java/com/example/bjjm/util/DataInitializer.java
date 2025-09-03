package com.example.bjjm.util;

import com.example.bjjm.entity.Badge;
import com.example.bjjm.entity.Mission;
import com.example.bjjm.entity.Puzzle;
import com.example.bjjm.entity.Theme;
import com.example.bjjm.repository.BadgeRepository;
import com.example.bjjm.repository.MissionRepository;
import com.example.bjjm.repository.PuzzleRepository;
import com.example.bjjm.repository.ThemeRepository;
import com.example.bjjm.service.S3Service;
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
    private final BadgeRepository badgeRepository;
    private final ThemeRepository themeRepository;
    private final ExcelPuzzleLoader excelPuzzleLoader;
    private final ExcelMissionLoader excelMissionLoader;
    private final ExcelThemeLoader excelThemeLoader;

    private final S3Service s3Service;

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

        // 퍼즐별 미션 수 업데이트
        List<Puzzle> puzzles = puzzleRepository.findAll();
        for (Puzzle puzzle : puzzles) {
            int missionCount = missionRepository.countByPuzzle(puzzle);
            puzzle.setTotalMissionCount(missionCount);
        }
        puzzleRepository.saveAll(puzzles);

        System.out.println(">>>>> 퍼즐별 totalMissionCount 업데이트 완료!");

        // 뱃지 초기화
        if (badgeRepository.count() == 0) {
            List<String> badgeCodes = List.of("LEVEL_1", "LEVEL_2", "LEVEL_3");

            List<Badge> badges = badgeCodes.stream()
                    .map(code -> Badge.builder()
                            .code(code)
                            .imageUrl(s3Service.getBadgeImageUrl(code))
                            .build())
                    .toList();

            badgeRepository.saveAll(badges);
            System.out.println(">>>>> 뱃지 데이터 초기화 완료 (" + badges.size() + "건)");
        } else {
            System.out.println(">>>>> 뱃지 데이터가 이미 존재합니다. 초기화 건너뜀.");
        }

        // 테마 초기화
        if (themeRepository.count() == 0) {
            try (InputStream inputStream = getClass().getResourceAsStream("/data/themes.xlsx")) {
                List<Theme> themes = excelThemeLoader.loadThemesFromExcel(inputStream);
                themeRepository.saveAll(themes);
                System.out.println(">>>>> 테마 데이터 초기화 완료 (" + themes.size() + "건)");
            }
        } else {
            System.out.println(">>>>> 테마 데이터가 이미 존재합니다.");
        }
    }
}


