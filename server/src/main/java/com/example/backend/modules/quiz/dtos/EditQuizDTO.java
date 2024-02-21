package com.example.backend.modules.quiz.dtos;


import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class EditQuizDTO {
    @Digits(integer = 10,fraction = 0,message = "Trường 'topicId' phải là số nguyên")
    @NotNull(message = "Không được thiếu trường 'quizId'")
    private int quizId;

    @NotBlank(message = "Không được bỏ trống trường 'summary'")
    @NotNull(message = "Không được thiếu trường 'summary'")
    @Length(max = 300,message = "Tài khoản có độ dài tối đa là 300 ký tự")
    private String summary;

    @NotBlank(message = "Không được bỏ trống trường 'description'")
    @NotNull(message = "Không được thiếu trường 'description'")
    @Length(max = 1000,message = "Tài khoản có độ dài tối đa là 1000 ký tự")
    private String description;

    @NotBlank(message = "Không được bỏ trống trường 'title'")
    @NotNull(message = "Không được thiếu trường 'title'")
    @Length(max = 150,message = "Tài khoản có độ dài tối đa là 150 ký tự")
    private String title;

    @NotBlank(message = "Không được bỏ trống trường 'slug'")
    @NotNull(message = "Không được thiếu trường 'slug'")
    private String slug;

    @Digits(integer = 10,fraction = 0,message = "Trường 'topicId' phải là số nguyên")
    private Integer topicId;
}
