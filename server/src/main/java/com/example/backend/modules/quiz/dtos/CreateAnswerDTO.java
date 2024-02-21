package com.example.backend.modules.quiz.dtos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class CreateAnswerDTO {
    @NotBlank(message = "Không được bỏ trống trường 'title'")
    @NotNull(message = "Không được thiếu trường 'title'")
    @Length(max = 150,message = "Tài khoản có độ dài tối đa là 150 ký tự")
    private String title;

    @Digits(integer = 10,fraction = 0,message = "Trường 'questionId' phải là số nguyên")
    @NotNull(message = "Không được thiếu trường 'questionId'")
    private int questionId;

    @NotNull(message = "Không được thiếu trường 'isCorrect'")
    private boolean isCorrect;
}
