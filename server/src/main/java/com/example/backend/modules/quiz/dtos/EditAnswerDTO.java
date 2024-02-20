package com.example.backend.modules.quiz.dtos;

import lombok.Getter;

@Getter
public class EditAnswerDTO {
    private String title;
    private boolean isCorrect;
    private int quizId;
    private int answerId;
}
