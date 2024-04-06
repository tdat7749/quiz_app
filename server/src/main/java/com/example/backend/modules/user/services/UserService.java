package com.example.backend.modules.user.services;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.user.dtos.*;
import com.example.backend.modules.user.models.User;
import com.example.backend.modules.user.viewmodels.UserDetailVm;
import com.example.backend.modules.user.viewmodels.UserVm;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public interface UserService {
    Optional<User> findByUserName(String userName);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndToken(String email,String token);

    void saveUser(User user);

    ResponseSuccess<String> changeAvatar(ChangeAvatarDTO dto, User user) throws IOException;

    ResponseSuccess<Boolean> changePassword(ChangePasswordDTO dto, User user);

    ResponseSuccess<String> changeDisplayName(ChangeDisplayNameDTO dto, User user);

    ResponseSuccess<UserVm> getMe(User user);

    ResponseSuccess<Boolean> forgotPassword(ForgotPasswordDTO dto);

    ResponseSuccess<Boolean> sendCodeForgotPassword(SendEmailForgotDTO dto);

    ResponseSuccess<UserDetailVm> getMeDetail(User user);
}
