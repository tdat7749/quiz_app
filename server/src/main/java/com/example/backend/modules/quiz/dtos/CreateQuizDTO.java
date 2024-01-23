package com.example.backend.modules.quiz.dtos;


import lombok.Getter;

@Getter
public class CreateQuizDTO {
    private String summary;
    private String description;
    private String thumbnail;
    private String title;
    private String slug;
    private int topicId;
}
