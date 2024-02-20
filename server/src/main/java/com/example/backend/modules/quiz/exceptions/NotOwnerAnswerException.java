package com.example.backend.modules.quiz.exceptions;

public class NotOwnerAnswerException extends RuntimeException {
    public NotOwnerAnswerException(String message){
        super(message);
    }
}
