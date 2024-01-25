package com.example.backend.modules.room.exceptions;



public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException(String message){
        super(message);
    }
}
