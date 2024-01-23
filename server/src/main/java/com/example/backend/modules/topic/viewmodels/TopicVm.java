package com.example.backend.modules.topic.viewmodels;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopicVm {
    private int id;
    private String title;
    private String thumbnail;
    private String createdAt;
    private String updatedAt;
    private String slug;
}
