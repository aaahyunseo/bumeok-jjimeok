package com.example.bjjm.exception;

import com.example.bjjm.exception.errorcode.ErrorCode;

public class ForbiddenException extends CustomException {
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
    public ForbiddenException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
