package com.example.backend.modules.quiz.dtos;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class EditQuizThumbnail {
    private MultipartFile thumbnail;
    private int quizId;
}
