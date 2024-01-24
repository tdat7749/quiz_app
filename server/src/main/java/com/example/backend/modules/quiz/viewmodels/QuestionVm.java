package com.example.backend.modules.quiz.viewmodels;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuestionVm {
    private int id;
    private String title;
    private QuestionTypeVm questionType;
    private String thumbnail;
    private int order;
    private int score;
    private int timeLimit;
    private List<AnswerVm> answers;
}