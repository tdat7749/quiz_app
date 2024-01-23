package com.example.backend.modules.topic.dtos;


import lombok.Getter;

@Getter
public class CreateTopicDTO {
    private String title;
    private String thumbnail;
    private String slug;
}
