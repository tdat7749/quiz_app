package com.example.backend.modules.room.exceptions;

public class RoomClosedException extends RuntimeException{
    public RoomClosedException(String message){
        super(message);
    }
}
