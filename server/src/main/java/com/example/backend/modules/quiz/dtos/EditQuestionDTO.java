package com.example.backend.modules.quiz.dtos;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class EditQuestionDTO {
    private int questionId;
    private String title;
    private MultipartFile thumbnail; // optional ?
    private int score;
    private int timeLimit;
    private int quizId;
}
