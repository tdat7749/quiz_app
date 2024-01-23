package com.example.backend.modules.quiz.viewmodels;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuizVm {
    private int id;
    private String title;
    private String slug;
    private String thumbnail;
}
