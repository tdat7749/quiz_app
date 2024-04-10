package com.example.backend.modules.quiz.viewmodels;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuestionVm {
    private int id;
    private String title;
    private String thumbnail;
    private int score;

    private List<AnswerVm> answers;
}
