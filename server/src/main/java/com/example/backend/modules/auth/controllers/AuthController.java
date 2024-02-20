package com.example.backend.modules.auth.controllers;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.auth.dtos.LoginDTO;
import com.example.backend.modules.auth.dtos.RegisterDTO;
import com.example.backend.modules.auth.services.AuthService;
import com.example.backend.modules.auth.viewmodels.AuthenVm;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping(value = "/register")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> register(@RequestBody @Valid RegisterDTO dto) {
        var result = authService.register(dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
