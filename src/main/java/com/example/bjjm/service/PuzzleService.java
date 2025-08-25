package com.example.bjjm.service;

import com.example.bjjm.dto.request.puzzle.MissionLocationAuthDto;
import com.example.bjjm.dto.request.puzzle.MissionRecordRequestDto;
import com.example.bjjm.dto.response.puzzle.*;
import com.example.bjjm.entity.*;
import com.example.bjjm.exception.NotFoundException;
import com.example.bjjm.exception.errorcode.ErrorCode;
import com.example.bjjm.repository.MissionRecordRepository;
import com.example.bjjm.repository.MissionRepository;
import com.example.bjjm.repository.PuzzleRepository;
import com.example.bjjm.repository.UserPuzzleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PuzzleService {

    private final PuzzleRepository puzzleRepository;
    private final UserPuzzleRepository userPuzzleRepository;
    private final MissionRepository missionRepository;
    private final MissionRecordRepository missionRecordRepository;

    /**
     * 지역별 퍼즐맵 진행 상황 조회
     * (지역 별 획득 퍼즐 수 / 총 갯수 , 부산 전체 퍼즐 획득 수, 완료 지역 구분)
     */
    public PuzzleMapProgressData getPuzzleMapProgress(User user) {
        List<UserPuzzle> userPuzzles = userPuzzleRepository.findAllByUser(user);

        if (userPuzzles.isEmpty()) {
            List<Puzzle> allPuzzles = puzzleRepository.findAll();

            userPuzzles = allPuzzles.stream()
                    .map(puzzle -> UserPuzzle.builder()
                            .user(user)
                            .puzzle(puzzle)
                            .collectedMissionCount(0)
                            .puzzleCompleted(false)
                            .build())
                    .map(userPuzzleRepository::save)
                    .toList();
        }

        return PuzzleMapProgressData.from(userPuzzles);
    }

    /**
     * 지역 관련 미션 목록 조회 (미션 제목, 미션 한줄 소개)
     */
    public PuzzleMapMissionListData getPuzzleMapMissions(User user, UUID puzzleId) {
        Puzzle puzzle = puzzleRepository.findById(puzzleId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PUZZLE_NOT_FOUND));
        List<Mission> missions = missionRepository.findAllByPuzzle(puzzle);
        return PuzzleMapMissionListData.of(missions, puzzle);
    }


    /**
     * 미션 상세 조회 (미션 사진, 미션 제목, 미션 한줄 소개, 미션 상세 내용)
     */
    public PuzzleMapMissionDetailDto getMissionDetail(UUID missionId) {
        Mission mission = findMissionById(missionId);
        return PuzzleMapMissionDetailDto.from(mission);
    }


    /**
     * 미션 별 유저들 미션 기록 목록 조회
     * (유저 이름, 유저 프로필, 등록일, 미션 사진, 미션 후기 내용)
     */
    public MissionRecordListData missionRecordList(UUID missionId) {
        Mission mission = findMissionById(missionId);
        List<MissionRecord> missionRecords = missionRecordRepository.findAllByMission(mission);
        return MissionRecordListData.from(missionRecords);
    }


    /**
     * 나의 미션 기록 목록 조회
     * (등록일, 미션 사진, 미션 후기 내용, 미션 제목, 미션 한줄 소개, 미션 진행 상황)
     */
    public MyMissionRecordListData getMyMissionRecords(User user) {
        List<MissionRecord> records = missionRecordRepository.findAllByUser(user);
        return MyMissionRecordListData.from(records);
    }


    /**
     * 미션 기록하기
     * 1. 위치 인증
     * 2. 사진 등록, 후기 작성
     */
    public void locationAuthentication(User user, UUID missionId, MissionLocationAuthDto dto) {
        Mission mission = findMissionById(missionId);

        double userX = Double.parseDouble(dto.getX());
        double userY = Double.parseDouble(dto.getY());

        double placeX = Double.parseDouble(mission.getX());
        double placeY = Double.parseDouble(mission.getY());

        double distance = calculateDistance(userY, userX, placeY, placeX);

        if (distance > 300) {
            throw new NotFoundException(ErrorCode.PLACE_NOT_FOUND);
        }
    }

    @Transactional
    public void writeMissionRecord(User user, UUID missionId, MissionRecordRequestDto requestDto) {
        Mission mission = findMissionById(missionId);

        MissionRecord newMissionRecord = MissionRecord.builder()
                .mission(mission)
                .user(user)
                .score(requestDto.getScore())
                .content(requestDto.getContent())
                .build();

        List<MissionRecordImage> images = requestDto.getImageFiles().stream()
                .map(imageUrl -> MissionRecordImage.builder()
                        .imageUrl(imageUrl)
                        .missionRecord(newMissionRecord)
                        .build())
                .toList();
        newMissionRecord.setImageFiles(images);

        missionRecordRepository.save(newMissionRecord);

        Puzzle puzzle = puzzleRepository.findById(mission.getPuzzle().getId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.PUZZLE_NOT_FOUND));

        UserPuzzle userPuzzle = userPuzzleRepository.findByUserAndPuzzle(user, puzzle)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PUZZLE_NOT_FOUND));

        int newCount = userPuzzle.getCollectedMissionCount() + 1;
        userPuzzle.setCollectedMissionCount(newCount);

        if (newCount >= puzzle.getTotalMissionCount()) userPuzzle.setPuzzleCompleted(true);

        userPuzzleRepository.save(userPuzzle);
    }

    /**
     * 퍼즐맵 미션 랭킹
     * (유저 이름, 유저 프로필, 유저 성공 미션 개수)
     */
    public MissionRankingResponseData getMissionRanking() {
        List<Object[]> results = missionRecordRepository.findUserMissionSuccessCounts();

        List<MissionRankingResponseDto> ranking = new ArrayList<>();

        int rank = 1;
        for (Object[] row : results) {
            User user = (User) row[0];
            Long successCount = (Long) row[1];

            ranking.add(new MissionRankingResponseDto(
                    user.getName(),
                    user.getProfileImage(),
                    successCount,
                    rank++
            ));
        }

        return MissionRankingResponseData.from(ranking);
    }


    // 현재 위치 오차 범위 100m 허용 (Haversine 공식)
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }


    public Mission findMissionById(UUID missionId) {
        return missionRepository.findById(missionId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MISSION_NOT_FOUND));
    }
}
