package com.example.bjjm.service;

import com.example.bjjm.dto.response.user.UserInfoResponseDto;
import com.example.bjjm.entity.User;
import com.example.bjjm.entity.UserPuzzle;
import com.example.bjjm.repository.UserBadgeRepository;
import com.example.bjjm.repository.UserPuzzleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserPuzzleRepository userPuzzleRepository;
    private final UserBadgeRepository userBadgeRepository;

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

        // 보유 뱃지 리스트
        List<String> badgeList = userBadgeRepository.findAllByUser(user).stream()
                .map(userBadge -> userBadge.getBadge().getImageUrl())
                .toList();

        // 메인 뱃지
        String mainBadge = userBadgeRepository.findByUserAndIsMainTrue(user)
                .map(userBadge -> userBadge.getBadge().getImageUrl())
                .orElse(null);

        return UserInfoResponseDto.of(user, completedMissionCount, collectedPuzzleCount, mainBadge, badgeList);
    }
}
