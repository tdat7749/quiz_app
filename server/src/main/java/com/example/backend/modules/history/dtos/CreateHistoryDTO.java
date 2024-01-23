package com.example.backend.modules.history.dtos;

import lombok.Getter;

import java.util.Date;


@Getter
public class CreateHistoryDTO {
    private int totalCorrect;
    private Date startedAt;
    private Date finishedAt;

    private int totalScore;

    private Integer quizId; // optional

    private Integer roomId; // optional

}
