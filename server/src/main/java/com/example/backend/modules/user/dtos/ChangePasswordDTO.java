package com.example.backend.modules.user.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
public class ChangePasswordDTO {
    @NotBlank(message = "Không được bỏ trống trường 'oldPassword'")
    @NotNull(message = "Không được thiếu trường 'oldPassword'")
    private String oldPassword;

    @NotBlank(message = "Không được bỏ trống trường 'newPassword'")
    @NotNull(message = "Không được thiếu trường 'newPassword'")
    @Length(max = 40,message = "Mật khẩu có độ dài tối đa là 40 ký tự")
    private String newPassword;


    @NotBlank(message = "Không được bỏ trống trường 'confirmPassword'")
    @NotNull(message = "Không được thiếu trường 'confirmPassword'")
    private String confirmPassword;
}
