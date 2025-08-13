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

    // ConflictException

    // ValidationException
    NOT_NULL("9001", "필수값이 누락되었습니다."),
    NOT_BLANK("9002", "필수값이 빈 값이거나 공백으로 되어있습니다."),
    REGEX("9003", "이메일 형식에 맞지 않습니다."),
    LENGTH("9004", "길이가 유효하지 않습니다.");

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
