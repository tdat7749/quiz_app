package com.example.backend.modules.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;



@Getter
public class RegisterDTO {
    @NotBlank(message = "Không được bỏ trống trường 'userName'")
    @NotNull(message = "Không được thiếu trường 'userName'")
    @Length(max = 40,message = "Tài khoản có độ dài tối đa là 40 ký tự")
    private String userName;

    @NotBlank(message = "Không được bỏ trống trường 'password'")
    @NotNull(message = "Không được thiếu trường 'password'")
    @Length(max = 40,message = "Mật khẩu có độ dài tối đa là 40 ký tự")
    private String password;

    @NotBlank(message = "Không được bỏ trống trường 'confirmPassword'")
    @NotNull(message = "Không được thiếu trường 'confirmPassword'")
    private String confirmPassword;

    @NotBlank(message = "Không được bỏ trống trường 'email'")
    @NotNull(message = "Không được thiếu trường 'email'")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Không được bỏ trống trường 'displayName'")
    @NotNull(message = "Không được thiếu trường 'displayName'")
    @Length(max = 60,message = "Họ có độ dài tối đa là 70 ký tự")
    private String displayName;

}
