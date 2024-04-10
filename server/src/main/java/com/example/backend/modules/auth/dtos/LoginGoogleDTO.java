package com.example.backend.modules.auth.dtos;


import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LoginGoogleDTO {
    @NotBlank(message = "Không được bỏ trống trường 'displayName'")
    @NotNull(message = "Không được thiếu trường 'displayName'")
    private String displayName;
    @NotBlank(message = "Không được bỏ trống trường 'email'")
    @NotNull(message = "Không được thiếu trường 'email'")
    private String email;
    @NotBlank(message = "Không được bỏ trống trường 'uid'")
    @NotNull(message = "Không được thiếu trường 'uid'")
    private String uid;

    @Nullable
    private String avatar;
}
