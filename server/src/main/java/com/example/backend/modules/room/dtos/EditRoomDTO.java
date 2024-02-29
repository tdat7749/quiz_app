package com.example.backend.modules.room.dtos;


import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class EditRoomDTO {
    private Date timeStart;
    private Date timeEnd;

    @NotNull(message = "Không được thiếu trường 'roomId'")
    @Digits(integer = 10,fraction = 0,message = "Trường 'roomId' phải là số nguyên")
    private int roomId;
}
