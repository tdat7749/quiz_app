package com.example.backend.modules.room.exceptions;

public class GameModeNotFoundException extends RuntimeException{
    public GameModeNotFoundException(String message){
        super(message);
    }
}
