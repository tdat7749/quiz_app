package com.example.backend.modules.quiz.dtos;


import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class CreateQuestionDTO {
    private String title;
    private int quizId;
    private int questionTypeId;
    private MultipartFile thumbnail;
    private int order;
    private int score;
    private int timeLimit;
    private List<CreateAnswerDTO> answers;
}
