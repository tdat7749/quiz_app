package com.example.backend.modules.quiz.dtos;

import com.example.backend.modules.quiz.viewmodels.AnswerVm;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@Builder
@Setter
public class EditAnswerDTO {
    @NotNull(message = "Không được thiếu trường 'anwers'")
    private List<AnswerVm> answers;

    @Digits(integer = 10,fraction = 0,message = "Trường 'quizId' phải là số nguyên")
    @NotNull(message = "Không được thiếu trường 'quizId'")
    private int quizId;
}
