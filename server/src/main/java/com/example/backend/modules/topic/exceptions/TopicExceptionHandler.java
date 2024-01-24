package com.example.backend.modules.topic.exceptions;

import com.example.backend.commons.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TopicExceptionHandler {

    @ExceptionHandler(TopicNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError topicNotFoundExceptionHandler(TopicNotFoundException ex){
        return new ResponseError(HttpStatus.NOT_FOUND,404,ex.getMessage());
    }
}
