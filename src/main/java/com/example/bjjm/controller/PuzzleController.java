package com.example.bjjm.controller;

import com.example.bjjm.authentication.AuthenticatedUser;
import com.example.bjjm.dto.ResponseDto;
import com.example.bjjm.dto.request.puzzle.MissionLocationAuthDto;
import com.example.bjjm.dto.request.puzzle.MissionRecordRequestDto;
import com.example.bjjm.dto.response.puzzle.*;
import com.example.bjjm.entity.User;
import com.example.bjjm.service.PuzzleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/puzzles")
public class PuzzleController {

    private final PuzzleService puzzleService;

    /**
     * 전체 지역별 퍼즐맵 진행 상황 조회
     * (지역 별 획득 퍼즐 수 / 총 갯수 , 부산 전체 퍼즐 획득 수, 완료 지역 구분)
     */
    @Operation(summary = "전체 지역별 퍼즐맵 진행 상황 조회", description = "전체 지역 별 퍼즐맵의 진행 상황을 조회합니다.")
    @GetMapping("/home")
    public ResponseEntity<ResponseDto<PuzzleMapProgressData>> getPuzzleMapProgress(@AuthenticatedUser User user) {
        PuzzleMapProgressData puzzleMapProgressData = puzzleService.getPuzzleMapProgress(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "전체 지역별 퍼즐맵 진행 상황 조회 완료", puzzleMapProgressData), HttpStatus.OK);
    }

    /**
     * 지역 관련 미션 목록 조회 (미션 제목, 미션 한줄 소개)
     */
    @Operation(summary = "지역 관련 미션 목록 조회", description = "지역 관련 미션 목록을 조회합니다.")
    @GetMapping("/mission/{puzzleRegionId}")
    public ResponseEntity<ResponseDto<PuzzleMapMissionListData>> getPuzzleMapMissions(@AuthenticatedUser User user, @PathVariable UUID puzzleRegionId) {
        PuzzleMapMissionListData puzzleMapMissionListData = puzzleService.getPuzzleMapMissions(user, puzzleRegionId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "지역 관련 미션 목록 조회 완료", puzzleMapMissionListData), HttpStatus.OK);
    }


    /**
     * 미션 상세 조회 (미션 사진, 미션 제목, 미션 한줄 소개, 미션 상세 내용)
     */
    @Operation(summary = "미션 상세 조회", description = "미션 상세 내용을 조회합니다.")
    @GetMapping("/mission/{missionId}")
    public ResponseEntity<ResponseDto<PuzzleMapMissionDetailDto>> getMissionDetail(@AuthenticatedUser User user, @PathVariable UUID missionId) {
        PuzzleMapMissionDetailDto puzzleMapMissionDetailDto = puzzleService.getMissionDetail(missionId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "미션 상세 조회 완료", puzzleMapMissionDetailDto), HttpStatus.OK);
    }


    /**
     * 미션 별 유저들 미션 기록 목록 조회
     * (유저 이름, 유저 프로필, 등록일, 미션 사진, 미션 후기 내용)
     */
    @Operation(summary = "미션 별 유저들 미션 기록 목록 조회", description = "미션 별 유저들 미션 기록 목록을 조회합니다.")
    @GetMapping("/mission/{missionId}/record")
    public ResponseEntity<ResponseDto<MissionRecordListData>> missionRecordList(@AuthenticatedUser User user, @PathVariable UUID missionId) {
        MissionRecordListData missionRecordListData = puzzleService.missionRecordList(missionId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "미션 별 유저들 미션 기록 목록 조회 완료", missionRecordListData), HttpStatus.OK);
    }


    /**
     * 나의 미션 기록 목록 조회
     * (등록일, 미션 사진, 미션 후기 내용, 미션 제목, 미션 한줄 소개, 미션 진행 상황)
     */
    @Operation(summary = "나의 미션 기록 목록 조회", description = "나의 미션 기록 목록을 조회합니다.")
    @GetMapping("/my-record")
    public ResponseEntity<ResponseDto<MyMissionRecordListData>> getMyMissionRecords(@AuthenticatedUser User user) {
        MyMissionRecordListData myMissionRecordListData = puzzleService.getMyMissionRecords(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "나의 미션 기록 목록 조회 완료", myMissionRecordListData), HttpStatus.OK);
    }

    /**
     * 미션 인증하기
     * 1. 위치 인증
     * 2. 사진 등록, 후기 작성
     */
    @Operation(summary = "위치 인증하기", description = "현재 나의 위치를 인증합니다.")
    @PostMapping("/mission/{missionId}/location")
    public ResponseEntity<ResponseDto<Void>> locationAuthentication(@AuthenticatedUser User user, @PathVariable UUID missionId, MissionLocationAuthDto requestDto) {
        puzzleService.locationAuthentication(user, missionId, requestDto);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED, "위치 인증 완료"), HttpStatus.CREATED);
    }

    @Operation(summary = "미션 기록하기", description = "미션 후기를 기록합니다.")
    @PostMapping("/mission/{missionId}/record")
    public ResponseEntity<ResponseDto<Void>> writeMissionRecord(@AuthenticatedUser User user, @PathVariable UUID missionId, MissionRecordRequestDto requestDto) {
        puzzleService.writeMissionRecord(user, missionId, requestDto);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED, "미션 기록 완료"), HttpStatus.CREATED);
    }

    /**
     * 퍼즐맵 미션 유저 랭킹 조회
     * (유저 이름, 유저 프로필, 유저 성공 미션 개수)
     */
    @Operation(summary = "퍼즐맵 미션 유저 랭킹 조회", description = "퍼즐맵 미션 유저들의 랭킹을 조회합니다.")
    @GetMapping("/my-record")
    public ResponseEntity<ResponseDto<MissionRankingResponseData>> getMissionRanking(@AuthenticatedUser User user) {
        MissionRankingResponseData missionRankingResponseData = puzzleService.getMissionRanking();
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "퍼즐맵 미션 유저 랭킹 조회 완료", missionRankingResponseData), HttpStatus.OK);
    }

}
