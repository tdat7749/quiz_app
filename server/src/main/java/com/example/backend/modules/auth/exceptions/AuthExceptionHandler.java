package com.example.backend.modules.auth.exceptions;


import com.example.backend.commons.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(EmailUsedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError emailUsedExceptionHandler(EmailUsedException ex) {
        return new ResponseError(HttpStatus.BAD_REQUEST, 400, ex.getMessage());
    }

    @ExceptionHandler(UserNameUsedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError userNameUsedExceptionHandler(UserNameUsedException ex) {
        return new ResponseError(HttpStatus.BAD_REQUEST, 400, ex.getMessage());
    }

    @ExceptionHandler(RegisterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError registerExceptionHandler(RegisterException ex) {
        return new ResponseError(HttpStatus.BAD_REQUEST, 400, ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError loginExceptionHandler(AuthenticationException ex) {
        return new ResponseError(HttpStatus.BAD_REQUEST, 400, ex.getMessage());
    }
}
