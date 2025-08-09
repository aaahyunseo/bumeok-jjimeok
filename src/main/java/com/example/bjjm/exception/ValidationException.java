package com.example.bjjm.exception;

import com.example.bjjm.exception.errorcode.ErrorCode;

public class ValidationException extends CustomException {
    public ValidationException(ErrorCode errorCode) {
        super(errorCode);
    }
    public ValidationException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
