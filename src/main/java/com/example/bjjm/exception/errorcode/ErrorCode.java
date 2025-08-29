package com.example.bjjm.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // BadRequestException

    // UnauthorizedException
    INVALID_TOKEN("4010", "유효하지 않은 토큰입니다."),

    // ForbiddenException
    NO_ACCESS("4030", "접근 권한이 없습니다."),

    // NotFoundException
    COOKIE_NOT_FOUND("4040", "쿠키를 찾을 수 없습니다."),
    USER_NOT_FOUND("4041", "유저를 찾을 수 없습니다."),
    THEME_NOT_FOUND("4042", "테마를 찾을 수 없습니다."),
    THEME_COMMENT_NOT_FOUND("4043", "테마 댓글을 찾을 수 없습니다."),
    THEME_SCRAP_NOT_FOUND("4044", "스크랩한 테마가 아닙니다."),
    PLACE_INFO_NOT_FOUND("4045", "장소 정보를 찾을 수 없습니다."),
    PUZZLE_NOT_FOUND("4046", "퍼즐 여부를 찾을 수 없습니다."),
    MISSION_NOT_FOUND("4047", "상세 미션을 찾을 수 없습니다."),
    PLACE_NOT_FOUND("4048", "장소의 위치 인증에 실패했습니다."),
    BADGE_NOT_FOUND("4049", "뱃지를 찾을 수 없습니다."),

    // ConflictException
    THEME_SCRAP_CONFLICT("4090", "이미 스크랩한 테마입니다."),

    // ValidationException
    NOT_NULL("9001", "필수값이 누락되었습니다."),
    NOT_BLANK("9002", "필수값이 빈 값이거나 공백으로 되어있습니다."),
    REGEX("9003", "이메일 형식에 맞지 않습니다."),
    LENGTH("9004", "길이가 유효하지 않습니다."),
    OFFICIAL_OR_USER("9005", "themeType 값은 official 또는 user 여야 합니다.");

    private final String code;
    private final String message;

    // Dto의 어노테이션을 통해 발생한 에러코드를 반환
    public static ErrorCode resolveValidationErrorCode(String code) {
        return switch (code) {
            case "NotNull" -> NOT_NULL;
            case "NotBlank" -> NOT_BLANK;
            case "Pattern" -> REGEX;
            case "Length" -> LENGTH;
            default -> throw new IllegalArgumentException("Unexpected value: " + code);
        };
    }
}
