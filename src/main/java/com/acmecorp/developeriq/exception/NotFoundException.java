package com.acmecorp.developeriq.exception;

import lombok.Getter;

/**
 * This class is to handle the 404 - NotFound errors.
 */
@Getter
public class NotFoundException extends RuntimeException {
    private final String code;

    public NotFoundException(String msg, String code) {
        super(msg);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
