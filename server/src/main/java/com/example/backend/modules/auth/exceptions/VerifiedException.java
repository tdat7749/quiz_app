package com.example.backend.modules.auth.exceptions;

public class VerifiedException extends RuntimeException{
    public VerifiedException(String message){
        super(message);
    }
}
