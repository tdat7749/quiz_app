package com.example.backend.modules.filestorage.exceptions;

public class FileIsEmptyException extends RuntimeException{
    public FileIsEmptyException(String message){
        super(message);
    }
}
