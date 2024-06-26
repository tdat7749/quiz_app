package com.example.backend.modules.history.dtos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Builder
@Getter
@Setter
public class CreateHistoryDTO {

    @NotNull(message = "Không được thiếu trường 'totalCorrect'")
    @Digits(integer = 10,fraction = 0,message = "Trường 'totalCorrect' phải là số nguyên")
    private int totalCorrect;
    private String startedAt;
    private String finishedAt;

    @NotNull(message = "Không được thiếu trường 'totalScore'")
    @Digits(integer = 10,fraction = 0,message = "Trường 'totalScore' phải là số nguyên")
    private int totalScore;

    @Digits(integer = 10,fraction = 0,message = "Trường 'quizId' phải là số nguyên")
    private Integer quizId; // optional

    @Digits(integer = 10,fraction = 0,message = "Trường 'roomId' phải là số nguyên")
    private Integer roomId; // optional

    @NotEmpty(message = "Danh sách câu trả lời không được trống")
    @NotNull(message = "Không được thiếu trường 'historyAnswers'")
    private List<CreateHistoryAnswerDTO> historyAnswers = new ArrayList<>();

}
