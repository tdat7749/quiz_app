package com.example.backend.modules.quiz.dtos;


import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuizDTO {
    @NotBlank(message = "Không được bỏ trống trường 'summary'")
    @NotNull(message = "Không được thiếu trường 'summary'")
    @Length(max = 300,message = "Tài khoản có độ dài tối đa là 300 ký tự")
    private String summary;

    @NotBlank(message = "Không được bỏ trống trường 'description'")
    @NotNull(message = "Không được thiếu trường 'description'")
    @Length(max = 1000,message = "Tài khoản có độ dài tối đa là 1000 ký tự")
    private String description;

    @NotNull(message = "Không được thiếu trường 'thumbnail'")
    private MultipartFile thumbnail;

    @NotBlank(message = "Không được bỏ trống trường 'title'")
    @NotNull(message = "Không được thiếu trường 'title'")
    @Length(max = 150,message = "Tài khoản có độ dài tối đa là 150 ký tự")
    private String title;

    @NotBlank(message = "Không được bỏ trống trường 'slug'")
    @NotNull(message = "Không được thiếu trường 'slug'")
    private String slug;

    @Digits(integer = 10,fraction = 0,message = "Trường 'topicId' phải là số nguyên")
    @NotNull(message = "Không được thiếu trường 'topicId'")
    private Integer topicId;

    @NotNull(message = "Không được thiếu trường 'questions'")
    private List<CreateQuestionDTO> questions;

    @NotNull(message = "Không được thiếu trường 'isPublic'")
    private String isPublic;

}
