package com.example.backend.modules.quiz.exceptions;

public class AnswerNotFoundException extends RuntimeException{
    public AnswerNotFoundException(String message){
        super(message);
    }
}
