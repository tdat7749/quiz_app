package com.example.backend.modules.user.exceptions;

public class PasswordIncorrectException extends RuntimeException {
    public PasswordIncorrectException(String message){
        super(message);
    }
}
