package com.example.backend.modules.user.exceptions;

import com.example.backend.commons.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError userNotFoundExceptionHandler(UserNotFoundException ex){
        return new ResponseError(HttpStatus.NOT_FOUND,404,ex.getMessage());
    }

    @ExceptionHandler(PasswordIncorrectException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError changePasswordExceptionHandler(PasswordIncorrectException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400, ex.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError invalidCodeExceptionHandler(InvalidTokenException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400, ex.getMessage());
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError forgotPasswordExceptionHandler(PasswordNotMatchException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400, ex.getMessage());
    }
}
