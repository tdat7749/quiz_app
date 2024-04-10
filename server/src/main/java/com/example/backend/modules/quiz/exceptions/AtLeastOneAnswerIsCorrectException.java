package com.example.backend.modules.quiz.exceptions;

public class AtLeastOneAnswerIsCorrectException extends RuntimeException{
    public AtLeastOneAnswerIsCorrectException(String message){
        super(message);
    }
}
