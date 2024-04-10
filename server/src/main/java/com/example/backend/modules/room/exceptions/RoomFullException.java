package com.example.backend.modules.room.exceptions;

public class RoomFullException extends RuntimeException{
    public RoomFullException(String message){
        super(message);
    }
}
