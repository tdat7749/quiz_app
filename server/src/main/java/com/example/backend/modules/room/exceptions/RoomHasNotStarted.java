package com.example.backend.modules.room.exceptions;

public class RoomHasNotStarted extends RuntimeException{
    public RoomHasNotStarted(String message){
        super(message);
    }
}
