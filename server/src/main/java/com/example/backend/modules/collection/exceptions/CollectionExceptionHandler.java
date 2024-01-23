package com.example.backend.modules.collection.exceptions;


import com.example.backend.commons.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CollectionExceptionHandler {
    @ExceptionHandler(CollectedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError collectedExceptionHandler(CollectedException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,ex.getMessage());
    }

    @ExceptionHandler(NotYetCollectedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError notYetCollectedExceptionHandler(NotYetCollectedException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,ex.getMessage());
    }
}
