package com.kakaopay.finance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "No Institute Data")
public class NoDataException extends Exception{
    private static final long serialVersionUID = 1L;

    public NoDataException(String message) {
        super(message);
    }

}
