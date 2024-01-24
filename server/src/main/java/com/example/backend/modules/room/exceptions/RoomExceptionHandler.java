package com.example.backend.modules.room.exceptions;


import com.example.backend.commons.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RoomExceptionHandler {
    @ExceptionHandler(RoomNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError roomNotFoundExceptionHandler(RoomNotFoundException ex){
        return new ResponseError(HttpStatus.NOT_FOUND,404,ex.getMessage());
    }

    @ExceptionHandler(RoomOwnerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError roomOwnerExceptionHandler(RoomOwnerException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,ex.getMessage());
    }

    @ExceptionHandler(RoomClosedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError roomClosedExceptionHandler(RoomClosedException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,ex.getMessage());
    }
}
