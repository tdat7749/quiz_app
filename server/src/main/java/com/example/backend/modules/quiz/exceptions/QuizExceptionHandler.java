package com.example.backend.modules.quiz.exceptions;

import com.example.backend.commons.ResponseError;
import com.example.backend.modules.collection.exceptions.CollectedException;
import com.example.backend.modules.quiz.models.Question;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class QuizExceptionHandler {

    @ExceptionHandler(QuizNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError quizNotFoundExceptionHandler(QuizNotFoundException ex){
        return new ResponseError(HttpStatus.NOT_FOUND,404,ex.getMessage());
    }

    @ExceptionHandler(QuestionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError questionNotFoundExceptionHandler(QuestionNotFoundException ex){
        return new ResponseError(HttpStatus.NOT_FOUND,404,ex.getMessage());
    }

    @ExceptionHandler(QuizNotPublicException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError quizNotPublicExceptionHandler(QuizNotPublicException ex){
        return new ResponseError(HttpStatus.NOT_FOUND,404,ex.getMessage());
    }

    @ExceptionHandler(QuizSlugUsedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError quizSlugUsedExceptionHandler(QuizSlugUsedException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,ex.getMessage());
    }

    @ExceptionHandler(QuestionTypeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError questionTypeNotFoundExceptionHandler(QuestionTypeNotFoundException ex){
        return new ResponseError(HttpStatus.NOT_FOUND,404,ex.getMessage());
    }

    @ExceptionHandler(NotOwnerQuizException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError notOwnerQuizExceptionHandler(NotOwnerQuizException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,ex.getMessage());
    }

    @ExceptionHandler(AnswerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError answerNotFoundExceptionHandler(AnswerNotFoundException ex){
        return new ResponseError(HttpStatus.NOT_FOUND,404,ex.getMessage());
    }
}
