package com.example.backend.modules.quiz.dtos;

import lombok.Getter;

@Getter
public class EditQuestionDTO {
    private String title;
    private String thumbnail; // optional ?
    private int score;
    private int timeLimit;
    private int quizId;
}
