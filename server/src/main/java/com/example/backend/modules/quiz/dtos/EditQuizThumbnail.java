package com.example.backend.modules.quiz.dtos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class EditQuizThumbnail {
    @NotNull(message = "Không được thiếu trường 'thumbnail'")
    private MultipartFile thumbnail;

    @Digits(integer = 10,fraction = 0,message = "Trường 'topicId' phải là số nguyên")
    @NotNull(message = "Không được thiếu trường 'quizId'")
    private int quizId;
}
