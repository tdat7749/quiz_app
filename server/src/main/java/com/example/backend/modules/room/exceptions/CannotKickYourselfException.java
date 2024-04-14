package com.example.backend.modules.room.exceptions;

public class CannotKickYourselfException extends RuntimeException{
    public CannotKickYourselfException(String message){
        super(message);
    }
}
