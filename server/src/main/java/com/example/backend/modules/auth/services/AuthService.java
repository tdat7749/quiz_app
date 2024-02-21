package com.example.backend.modules.auth.services;


import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.auth.dtos.LoginDTO;
import com.example.backend.modules.auth.dtos.RegisterDTO;
import com.example.backend.modules.auth.dtos.ResendMailDTO;
import com.example.backend.modules.auth.dtos.VerifyDTO;
import com.example.backend.modules.auth.viewmodels.AuthenVm;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    public ResponseSuccess<AuthenVm> login(LoginDTO dto);

    public ResponseSuccess<Boolean> register(RegisterDTO dto);

    public ResponseSuccess<Boolean> verifyAccount(VerifyDTO dto);

    public ResponseSuccess<Boolean> resendEmail(ResendMailDTO dto);

    public ResponseSuccess<String> refreshToken(String refreshToken);

}
