package com.example.backend.modules.quiz.exceptions;

public class QuizSlugUsedException extends RuntimeException{
    public QuizSlugUsedException(String message){
        super(message);
    }
}
