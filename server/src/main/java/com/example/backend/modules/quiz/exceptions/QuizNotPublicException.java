package com.example.backend.modules.quiz.exceptions;

public class QuizNotPublicException extends RuntimeException{
   public QuizNotPublicException(String message){
       super(message);
   }
}
