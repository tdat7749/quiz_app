package com.example.backend.modules.quiz.exceptions;

public class QuestionTypeNotFoundException extends RuntimeException{
    public QuestionTypeNotFoundException(String message){
        super(message);
    }
}
