package com.example.backend.modules.auth.exceptions;

public class EmailNotFoundException extends RuntimeException{
    public EmailNotFoundException(String message){
        super(message);
    }
}
