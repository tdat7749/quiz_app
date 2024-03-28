package com.example.backend.modules.user.controllers;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.user.dtos.*;
import com.example.backend.modules.user.models.User;
import com.example.backend.modules.user.services.UserService;
import com.example.backend.modules.user.viewmodels.UserVm;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {
    private UserService userService;

    public UserController(
            UserService userService
    ){
        this.userService = userService;
    }

    @PatchMapping("/password")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> changePassword(@RequestBody @Valid ChangePasswordDTO dto,
                                                                   @AuthenticationPrincipal User user) {
        var result = userService.changePassword(dto, user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/name")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<String>> changeDisplayName(@RequestBody @Valid ChangeDisplayNameDTO dto,
                                                                           @AuthenticationPrincipal User user) {
        var result = userService.changeDisplayName(dto, user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/avatar")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<String>> changeAvatar(@ModelAttribute @Valid ChangeAvatarDTO dto,
                                                                @AuthenticationPrincipal User user) throws IOException {
        var result = userService.changeAvatar(dto, user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/forgot-mail")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> sendCodeForgotPassword(
            @RequestBody SendEmailForgotDTO dto) {
        var result = userService.sendCodeForgotPassword(dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/forgot")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> forgotPassword(
            @RequestBody ForgotPasswordDTO dto) {
        var result = userService.forgotPassword(dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/me")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<UserVm>> getMe(@AuthenticationPrincipal User user) {
        var result = userService.getMe(user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
