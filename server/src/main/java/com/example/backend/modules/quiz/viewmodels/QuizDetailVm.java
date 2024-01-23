package com.example.backend.modules.quiz.viewmodels;


import com.example.backend.modules.topic.viewmodels.TopicVm;
import com.example.backend.modules.user.viewmodels.UserVm;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuizDetailVm {
    private int id;
    private String summary;
    private String description;
    private String thumbnail;
    private String title;
    private boolean isPublic;
    private String slug;
    private String createdAt;
    private String updatedAt;
    private UserVm user;
    private TopicVm topic;
    private int totalScore;
}
