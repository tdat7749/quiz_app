package com.example.backend.modules.history.exceptions;

public class CannotPlayAgainException extends RuntimeException{
    public CannotPlayAgainException(String message){
        super(message);
    }
}
