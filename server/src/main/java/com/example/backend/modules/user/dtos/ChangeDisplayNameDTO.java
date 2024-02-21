package com.example.backend.modules.user.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
public class ChangeDisplayNameDTO {

    @NotBlank(message = "Không được bỏ trống trường 'displayName'")
    @NotNull(message = "Không được thiếu trường 'displayName'")
    @Length(max = 50,message = "Mật khẩu có độ dài tối đa là 50 ký tự")
    private String displayName;
}
