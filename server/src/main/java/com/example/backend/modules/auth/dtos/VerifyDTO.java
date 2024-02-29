package com.example.backend.modules.auth.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class VerifyDTO {
    @NotNull(message = "Không được thiếu trường 'email'")
    @NotBlank(message = "Không được để trống trường 'email'")
    private String email;
    @NotNull(message = "Không được thiếu trường 'token'")
    @NotBlank(message = "Không được để trống trường 'token'")
    private String token;
}
