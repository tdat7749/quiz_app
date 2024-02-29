package com.example.backend.modules.history.dtos;


import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CreateHistoryAnswerDTO {

    @NotNull(message = "Không được thiếu trường 'questionId'")
    @Digits(integer = 10,fraction = 0,message = "Trường 'questionId' phải là số nguyên")
    private int questionId;

    @NotNull(message = "Không được thiếu trường 'isCorrect'")
    @Digits(integer = 10,fraction = 0,message = "Trường 'isCorrect' phải là số nguyên")
    private Boolean isCorrect;
}
