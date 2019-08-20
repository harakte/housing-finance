package com.kakaopay.finance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Institute Not Found")
public class InstituteNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public InstituteNotFoundException(String message) {
        super(message);
    }
}
