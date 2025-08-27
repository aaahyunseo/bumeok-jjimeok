package com.example.bjjm.service;

import com.example.bjjm.dto.response.user.UserInfoResponseDto;
import com.example.bjjm.entity.User;
import com.example.bjjm.entity.UserPuzzle;
import com.example.bjjm.repository.UserPuzzleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserPuzzleRepository userPuzzleRepository;

    public UserInfoResponseDto getUserInfo(User user) {
        List<UserPuzzle> userPuzzles = userPuzzleRepository.findAllByUser(user);

        int collectedPuzzleCount = 0;
        int completedMissionCount = 0;

        for (UserPuzzle userPuzzle : userPuzzles) {
            // 유저의 퍼즐 완료 여부
            if (userPuzzle.getPuzzleCompleted()) collectedPuzzleCount++;

            // 유저가 완료한 미션 수
            completedMissionCount += userPuzzle.getCollectedMissionCount();
        }

        return UserInfoResponseDto.of(user, completedMissionCount, collectedPuzzleCount);
    }
}
