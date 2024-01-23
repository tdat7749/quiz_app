package com.example.backend.modules.auth.exceptions;

public class RegisterException extends RuntimeException{
    public RegisterException(String message){
        super(message);
    }
}
