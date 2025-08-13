package com.example.bjjm.exception.dto;

import com.example.bjjm.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {
    private final String errorCode;
    private final String message;
    private final String detail;

    public static ErrorResponseDto res(final CustomException customException) {
        String errorCode = customException.getErrorCode().getCode();
        String message = customException.getErrorCode().getMessage();
        String detail = customException.getDetail();
        return new ErrorResponseDto(errorCode, message, detail);
    }

    //예상치못한 예외 발생
    public static ErrorResponseDto res(final String errorCode, final Exception exception) {
        return new ErrorResponseDto(errorCode, exception.getMessage(), null);
    }
}
