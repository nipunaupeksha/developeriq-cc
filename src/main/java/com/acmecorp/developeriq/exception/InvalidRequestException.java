package com.acmecorp.developeriq.exception;

import lombok.Getter;
import lombok.ToString;

/**
 * This is to handle the 400 - Bad Request Exceptions.
 */
@Getter
@ToString
public class InvalidRequestException extends RuntimeException {
    private final String code;

    public InvalidRequestException(String errorMessage, String code) {
        super(errorMessage);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
