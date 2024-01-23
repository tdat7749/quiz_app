package com.example.backend.modules.filestorage.exceptions;

public class NotAllowMimeTypeException extends RuntimeException{
    public NotAllowMimeTypeException(String message){
        super(message);
    }
}
