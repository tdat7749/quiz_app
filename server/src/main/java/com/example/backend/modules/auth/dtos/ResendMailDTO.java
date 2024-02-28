package com.example.backend.modules.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ResendMailDTO {
    @NotBlank(message = "Không được bỏ trống trường 'email'")
    @NotNull(message = "Không được thiếu trường 'email'")
    @Email(message = "Email không đúng định dạng")
    private String email;
}