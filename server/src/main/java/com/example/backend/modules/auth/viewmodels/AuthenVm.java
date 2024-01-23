package com.example.backend.modules.auth.viewmodels;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthenVm {
    private String accessToken;
    private String refreshToken;
}
