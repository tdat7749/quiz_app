package com.example.backend.modules.filestorage.exceptions;

public class FileTooLargeException extends RuntimeException{
    public FileTooLargeException(String message){
        super(message);
    }
}
