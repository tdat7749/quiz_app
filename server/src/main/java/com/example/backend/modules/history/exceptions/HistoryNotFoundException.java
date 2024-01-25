package com.example.backend.modules.history.exceptions;

public class HistoryNotFoundException extends RuntimeException{
    public HistoryNotFoundException(String message){
        super(message);
    }
}
