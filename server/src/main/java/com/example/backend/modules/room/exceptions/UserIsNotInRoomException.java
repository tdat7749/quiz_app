package com.example.backend.modules.room.exceptions;

public class UserIsNotInRoomException extends RuntimeException{
    public UserIsNotInRoomException(String message){
        super(message);
    }
}
