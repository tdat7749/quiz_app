package com.example.backend.modules.quiz.dtos;


import lombok.Getter;

@Getter
public class CreateQuestionDTO {
    private String title;
    private int quizId;
    private int questionTypeId;
    private String thumbnail;
    private int order;
    private int score;
    private int timeLimit;
}
