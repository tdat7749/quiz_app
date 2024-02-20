package com.example.backend.modules.quiz.dtos;


import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class CreateQuizDTO {
    private String summary;
    private String description;
    private MultipartFile thumbnail;
    private String title;
    private String slug;
    private int topicId;
    private List<CreateQuestionDTO> questions;
    private boolean isPublic;

}
