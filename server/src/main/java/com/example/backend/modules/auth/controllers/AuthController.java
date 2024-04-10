package com.example.backend.modules.auth.controllers;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.auth.dtos.*;
import com.example.backend.modules.auth.services.AuthService;
import com.example.backend.modules.auth.viewmodels.AuthenVm;
import com.google.firebase.auth.FirebaseAuthException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth") // http://localhost:8080/api/auth
public class AuthController {
    private final AuthService authService;

    public AuthController(
            AuthService authService
    ){
        this.authService = authService;
    }

    //
    @PostMapping(value = "/login")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<AuthenVm>> login(@RequestBody @Valid LoginDTO dto) {
        var result = authService.login(dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/login/google")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<AuthenVm>> loginWithGoogle(
            @RequestParam("token") String token
    ) throws FirebaseAuthException {
        var result = authService.loginWithGoogle(token);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> register(@RequestBody @Valid RegisterDTO dto) {
        var result = authService.register(dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/refresh")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<String>> refreshToken(@RequestHeader("Rftoken") String refreshToken) {
        var result = authService.refreshToken(refreshToken);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/logout")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> logout() {
        SecurityContextHolder.clearContext();

        return new ResponseEntity<>(new ResponseSuccess<Boolean>("Thành công", true), HttpStatus.OK);
    }

    @PostMapping(value = "/verify")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> verifyAccount(@RequestBody @Valid VerifyDTO dto) {
        var result = authService.verifyAccount(dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/resend")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> resendEmail(@RequestBody @Valid ResendMailDTO dto) {
        var result = authService.resendEmail(dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
