package com.example.backend.modules.quiz.viewmodels;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnswerVm {
    private int id;
    private String title;
    private boolean isCorrect;
}
