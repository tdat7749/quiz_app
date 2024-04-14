package com.example.backend.modules.auth.exceptions;

public class PasswordDoNotMatchException extends RuntimeException{
    public PasswordDoNotMatchException(String message){
        super(message);
    }
}
