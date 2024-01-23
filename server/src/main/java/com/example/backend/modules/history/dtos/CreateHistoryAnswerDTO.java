package com.example.backend.modules.history.dtos;


import lombok.Getter;

@Getter
public class CreateHistoryAnswerDTO {
    private int historyId;
    private int answerId;
    private Boolean isCorrect;

}
