package com.example.backend.modules.auth.services;


import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.auth.dtos.*;
import com.example.backend.modules.auth.viewmodels.AuthenVm;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    public ResponseSuccess<AuthenVm> login(LoginDTO dto);

    public ResponseSuccess<AuthenVm> loginWithGoogle(String token) throws FirebaseAuthException;

    public ResponseSuccess<Boolean> register(RegisterDTO dto);

    public ResponseSuccess<Boolean> verifyAccount(VerifyDTO dto);

    public ResponseSuccess<Boolean> resendEmail(ResendMailDTO dto);

    public ResponseSuccess<String> refreshToken(String refreshToken);

}
