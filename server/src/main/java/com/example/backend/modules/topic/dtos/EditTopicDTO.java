package com.example.backend.modules.topic.dtos;


import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class EditTopicDTO {

    @NotNull(message = "Không được thiếu trường 'topicId'")
    @Digits(integer = 10,fraction = 0,message = "Trường 'topicId' phải là số nguyên")
    private int topicId;

    @NotBlank(message = "Không được bỏ trống trường 'title'")
    @NotNull(message = "Không được thiếu trường 'title'")
    @Length(max = 150,message = "Độ dài trường 'title' tối đa là 150 ký tự")
    private String title;

    private MultipartFile thumbnail;

    @NotBlank(message = "Không được bỏ trống trường 'slug'")
    @NotNull(message = "Không được thiếu trường 'slug'")
    @Length(max = 170,message = "Độ dài trường 'slug' tối đa là 170 ký tự")
    private String slug;
}
