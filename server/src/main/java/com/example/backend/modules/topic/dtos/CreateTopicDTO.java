package com.example.backend.modules.topic.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@Setter
public class CreateTopicDTO {

    @NotBlank(message = "Không được bỏ trống trường 'title'")
    @NotNull(message = "Không được thiếu trường 'title'")
    @Length(max = 150,message = "Độ dài trường 'title' tối đa là 150 ký tự")
    private String title;

    @NotNull(message = "Không được thiếu trường 'thumbnail'")
    private MultipartFile thumbnail;

    @NotBlank(message = "Không được bỏ trống trường 'slug'")
    @NotNull(message = "Không được thiếu trường 'slug'")
    @Length(max = 170,message = "Độ dài trường 'slug' tối đa là 170 ký tự")
    private String slug;
}
