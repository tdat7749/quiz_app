package com.example.backend.modules.history.exceptions;


import com.example.backend.commons.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HistoryExceptionHandler {

    @ExceptionHandler(HistoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError historyNotFoundException(HistoryNotFoundException ex){
        return new ResponseError(HttpStatus.NOT_FOUND,404,ex.getMessage());
    }

    @ExceptionHandler(CannotPlayAgainException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError cannotPlayAgainException(CannotPlayAgainException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,ex.getMessage());
    }
}
