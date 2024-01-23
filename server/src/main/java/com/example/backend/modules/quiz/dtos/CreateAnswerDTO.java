package com.example.backend.modules.quiz.dtos;

import lombok.Getter;

@Getter
public class CreateAnswerDTO {
    private String title;
    private int questionId;
    private boolean isCorrect;
}
