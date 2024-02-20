package com.example.backend.modules.quiz.exceptions;

public class NotOwnerQuizException extends RuntimeException{
    public NotOwnerQuizException(String message){
        super(message);
    }
}
