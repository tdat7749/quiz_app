package com.example.backend.modules.user.exceptions;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(String message){
        super(message);
    }
}
