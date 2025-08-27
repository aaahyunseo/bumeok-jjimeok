package com.example.bjjm.dto.response.user;

import com.example.bjjm.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponseDto {
    private String name;
    private String email;
    private String profileImage;
    private int completedMissionCount;
    private int collectedPuzzleCount;

    public static UserInfoResponseDto of(User user, int completedMissionCount, int collectedPuzzleCount) {
        return UserInfoResponseDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .profileImage(user.getProfileImage())
                .collectedPuzzleCount(collectedPuzzleCount)
                .completedMissionCount(completedMissionCount)
                .build();
    }
}
