package com.example.backend.modules.auth.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Getter
public class LoginDTO {
    @NotBlank(message = "Không được bỏ trống trường 'userName'")
    @NotNull(message = "Không được thiếu trường 'userName'")
    private String userName;

    @NotBlank(message = "Không được bỏ trống trường 'password'")
    @NotNull(message = "Không được thiếu trường 'password'")
    private String password;
}
