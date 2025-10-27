package com.example.product_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    PRODUCT_EXISTED(1001,"Product Id is existed",HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_EXISTED(1002,"Product Id is not existed",HttpStatus.BAD_REQUEST),

    ;
    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;
    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

}
