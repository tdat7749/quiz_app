package com.example.backend.modules.user.exceptions;

public class PasswordNotMatchException extends RuntimeException{
    public PasswordNotMatchException(String message){
        super(message);
    }
}
