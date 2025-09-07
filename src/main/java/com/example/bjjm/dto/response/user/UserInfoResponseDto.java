package com.example.bjjm.dto.response.user;

import com.example.bjjm.entity.User;
import com.example.bjjm.entity.UserBadge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponseDto {
    private String name;
    private String email;
    private String profileImage;
    private String mainBadge;
    private String mainBadgeName;
    private List<String> badgeList;
    private int completedMissionCount;
    private int collectedPuzzleCount;

    public static UserInfoResponseDto of(User user, int completedMissionCount, int collectedPuzzleCount, UserBadge mainBadge, List<String> badgeList) {
        return UserInfoResponseDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .profileImage(user.getProfileImage())
                .mainBadge(mainBadge.getBadge().getImageUrl())
                .mainBadgeName(mainBadge.getBadge().getName())
                .badgeList(badgeList)
                .collectedPuzzleCount(collectedPuzzleCount)
                .completedMissionCount(completedMissionCount)
                .build();
    }
}
