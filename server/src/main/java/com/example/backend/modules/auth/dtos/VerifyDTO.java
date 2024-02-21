package com.example.backend.modules.auth.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class VerifyDTO {
    @NotNull(message = "Không được thiếu trường 'email'")
    @NotBlank(message = "Không được để trống trường 'email'")
    private String email;
    @NotNull(message = "Không được thiếu trường 'code'")
    @NotBlank(message = "Không được để trống trường 'code'")
    private String code;
}
