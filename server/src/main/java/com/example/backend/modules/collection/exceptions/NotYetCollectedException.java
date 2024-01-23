package com.example.backend.modules.collection.exceptions;

public class NotYetCollectedException extends RuntimeException{
    public NotYetCollectedException(String message){
        super(message);
    }
}
