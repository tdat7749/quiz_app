package com.example.backend.modules.auth.exceptions;

public class VerifyEmailException extends RuntimeException{
    public VerifyEmailException(String message){
        super(message);
    }
}
