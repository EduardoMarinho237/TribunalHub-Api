package com.eduardo.tribunalhub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) //400
public class EmailJaExistenteException extends RuntimeException {
    public EmailJaExistenteException() {
        super("Email já existente");
    }
    public EmailJaExistenteException(String message) {
        super(message);
    }
}
