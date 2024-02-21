package com.example.backend.modules.email.exceptions;


import com.example.backend.commons.ResponseError;
import jakarta.mail.SendFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EmailExceptionHandler {
    @ExceptionHandler(SendFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError sendFailedExceptionHandler(SendFailedException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,"Gửi mail thất bại");
    }
}
