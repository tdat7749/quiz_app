package com.example.backend.modules.room.dtos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Getter
public class CreateRoomDTO {
    @NotNull(message = "Không được thiếu trường 'quizId'")
    @Digits(integer = 10,fraction = 0,message = "Trường 'quizId' phải là số nguyên")
    private int quizId;


    private Date timeStart;
    private Date timeEnd;
}
