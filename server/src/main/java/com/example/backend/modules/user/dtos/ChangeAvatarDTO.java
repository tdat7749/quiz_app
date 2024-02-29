package com.example.backend.modules.user.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@Setter
public class ChangeAvatarDTO {
    @NotNull(message = "Không được thiếu trường 'avatar'")
    private MultipartFile avatar;
}
