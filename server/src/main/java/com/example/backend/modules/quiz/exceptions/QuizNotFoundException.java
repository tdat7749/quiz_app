package com.example.backend.modules.quiz.exceptions;



public class QuizNotFoundException extends RuntimeException{
    public QuizNotFoundException(String message){
        super(message);
    }
}
