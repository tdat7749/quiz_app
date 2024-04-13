package com.example.backend.modules.room.dtos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
@Getter
@Setter
public class CreateRoomDTO {
    @NotNull(message = "Không được thiếu trường 'quizId'")
    @Digits(integer = 10,fraction = 0,message = "Trường 'quizId' phải là số nguyên")
    private int quizId;

    @NotNull(message = "Không được thiếu trường 'modeId'")
    @Digits(integer = 10,fraction = 0,message = "Trường 'modeId' phải là số nguyên")
    private int modeId;

    @NotBlank(message = "Không được bỏ trống trường 'roomName'")
    @NotNull(message = "Không được thiếu trường 'roomName'")
    @Length(max = 100,message = "Tài khoản có độ dài tối đa là 100 ký tự")
    private String roomName;

    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;

    @NotNull(message = "Không được thiếu trường 'maxUser'")
    @Digits(integer = 10,fraction = 0,message = "Trường 'maxUser' phải là số nguyên")
    private int maxUser;

    @NotNull(message = "Không được thiếu trường 'playAgain'")
    private boolean playAgain;
}
