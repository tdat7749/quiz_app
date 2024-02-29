package com.example.backend.modules.quiz.dtos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@Setter
public class EditQuestionDTO {
    @Digits(integer = 10,fraction = 0,message = "Trường 'questionId' phải là số nguyên")
    private int questionId;

    @NotBlank(message = "Không được bỏ trống trường 'title'")
    @NotNull(message = "Không được thiếu trường 'title'")
    @Length(max = 150,message = "Tài khoản có độ dài tối đa là 150 ký tự")
    private String title;

    private MultipartFile thumbnail; // optional ?

    @Digits(integer = 10,fraction = 0,message = "Trường 'score' phải là số nguyên")
    @NotNull(message = "Không được thiếu trường 'score'")
    private int score;
    @Digits(integer = 10,fraction = 0,message = "Trường 'timeLimit' phải là số nguyên")
    @NotNull(message = "Không được thiếu trường 'timeLimit'")
    private int timeLimit;
    @Digits(integer = 10,fraction = 0,message = "Trường 'quizId' phải là số nguyên")
    @NotNull(message = "Không được thiếu trường 'quizId'")
    private int quizId;
}
