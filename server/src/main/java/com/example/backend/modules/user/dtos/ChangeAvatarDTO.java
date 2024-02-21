package com.example.backend.modules.user.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class ChangeAvatarDTO {
    @NotNull(message = "Không được thiếu trường 'avatar'")
    private MultipartFile avatar;
}
