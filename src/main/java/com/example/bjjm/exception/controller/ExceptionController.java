package com.example.bjjm.exception.controller;

import com.example.bjjm.exception.*;
import com.example.bjjm.exception.dto.ErrorResponseDto;
import com.example.bjjm.exception.errorcode.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.bjjm.exception.dto.ErrorResponseDto.res;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    // ConflictException 핸들러
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicationException(
            ConflictException duplicationException) {

        this.writeLog(duplicationException);
        return new ResponseEntity<>(res(duplicationException), HttpStatus.CONFLICT);
    }

    // ForbiddenException 핸들러
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponseDto> handleForbiddenException(
            ForbiddenException forbiddenException) {

        this.writeLog(forbiddenException);
        return new ResponseEntity<>(res(forbiddenException), HttpStatus.FORBIDDEN);
    }

    // NotFoundException 핸들러
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(
            NotFoundException notFoundException) {

        this.writeLog(notFoundException);
        return new ResponseEntity<>(res(notFoundException), HttpStatus.NOT_FOUND);
    }

    // UnauthorizedException  핸들러
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDto> handleUnauthorizedException(
            UnauthorizedException unauthorizedException) {

        this.writeLog(unauthorizedException);
        return new ResponseEntity<>(res(unauthorizedException), HttpStatus.UNAUTHORIZED);
    }

    // BadRequestException  핸들러
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleConflictException(BadRequestException badRequestException) {
        writeLog(badRequestException);
        return new ResponseEntity<>(res(badRequestException), HttpStatus.BAD_REQUEST);
    }

    // Dto 검증 어노테이션 예외 제외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        FieldError fieldError = methodArgumentNotValidException.getBindingResult().getFieldError();
        if (fieldError == null) {
            return new ResponseEntity<>(res(String.valueOf(HttpStatus.BAD_REQUEST.value())
                    , methodArgumentNotValidException), HttpStatus.BAD_REQUEST);
        }
        ErrorCode errorCode = ErrorCode.resolveValidationErrorCode(fieldError.getCode());
        String detail = fieldError.getDefaultMessage();
        ValidationException validationException = new ValidationException(errorCode, detail);

        this.writeLog(validationException);

        return new ResponseEntity<>(res(validationException), HttpStatus.BAD_REQUEST);
    }

    // 원인을 알 수 없는 예외 처리
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception exception) {
        this.writeLog(exception);
        return new ResponseEntity<>(
                res(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), exception),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void writeLog(CustomException customException) {
        String exceptionName = customException.getClass().getSimpleName();
        ErrorCode errorCode = customException.getErrorCode();
        String detail = customException.getDetail();

        log.error("({}){}: {}", exceptionName, errorCode.getMessage(), detail);
    }

    private void writeLog(Exception exception) {
        log.error("({}){}", exception.getClass().getSimpleName(), exception.getMessage());
    }
}
