package com.example.backend.modules.quiz.dtos;


import lombok.Getter;

@Getter
public class EditQuizDTO {
    private String summary;
    private String description;
    private String title;
    private String slug;
    private int topicId;
}
