package com.example.backend.modules.quiz.dtos;


import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuestionDTO {
    @NotBlank(message = "Không được bỏ trống trường 'title'")
    @NotNull(message = "Không được thiếu trường 'title'")
    @Length(max = 150,message = "Tài khoản có độ dài tối đa là 150 ký tự")
    private String title;


    @Digits(integer = 10,fraction = 0,message = "Trường 'questionTypeId' phải là số nguyên")
    @NotNull(message = "Không được thiếu trường 'questionTypeId'")
    private int questionTypeId;

    private MultipartFile thumbnail;

    @Digits(integer = 10,fraction = 0,message = "Trường 'order' phải là số nguyên")
    private int order;
    @Digits(integer = 10,fraction = 0,message = "Trường 'score' phải là số nguyên")
    private int score;
    @Digits(integer = 10,fraction = 0,message = "Trường 'timeLimit' phải là số nguyên")
    private int timeLimit;

    @NotNull(message = "Không được thiếu trường 'answers'")
    private List<CreateAnswerDTO> answers = new ArrayList<>();
}
