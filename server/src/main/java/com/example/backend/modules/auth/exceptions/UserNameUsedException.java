package com.example.backend.modules.auth.exceptions;

public class UserNameUsedException extends RuntimeException{
    public UserNameUsedException(String message){
        super(message);
    }
}
