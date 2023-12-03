package com.acmecorp.developeriq.exception;

import lombok.Getter;
import lombok.ToString;

/**
 * This class is to handle the 500 - Server Side Exceptions.
 */
@Getter
@ToString
public class TechnicalErrorException extends RuntimeException {
    private final String code;

    public TechnicalErrorException(String msg, String code) {
        super(msg);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
