package com.example.backend.modules.quiz.exceptions;

public class QuizHasNotQuestions extends RuntimeException{
    public QuizHasNotQuestions(String message){
        super(message);
    }
}
