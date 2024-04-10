package com.example.backend.modules.auth.exceptions;

public class LoginException extends RuntimeException{
    public LoginException(String message){
        super(message);
    }
}
