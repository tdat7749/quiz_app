package com.example.backend.modules.user.services;

import com.example.backend.commons.AppConstants;
import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.auth.constant.AuthConstants;
import com.example.backend.modules.auth.exceptions.EmailNotFoundException;
import com.example.backend.modules.email.services.EmailService;
import com.example.backend.modules.filestorage.services.FileStorageService;
import com.example.backend.modules.user.dtos.*;
import com.example.backend.modules.user.exceptions.InvalidTokenException;
import com.example.backend.modules.user.exceptions.PasswordIncorrectException;
import com.example.backend.modules.user.exceptions.PasswordNotMatchException;
import com.example.backend.modules.user.exceptions.UserNotFoundException;
import com.example.backend.modules.user.repositories.UserRepository;
import com.example.backend.modules.user.constant.UserConstants;
import com.example.backend.modules.user.models.User;
import com.example.backend.modules.user.viewmodels.UserVm;
import com.example.backend.utils.Utilities;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    public UserServiceImpl(
            UserRepository userRepository,
            @Lazy FileStorageService fileStorageService,
            @Lazy PasswordEncoder passwordEncoder,
            EmailService emailService
    ){
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }


    @Override
    public Optional<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByEmailAndToken(String email, String token) {
        return userRepository.findByEmailAndToken(email,token);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }



    @Override
    public ResponseSuccess<Boolean> sendCodeForgotPassword(SendEmailForgotDTO dto) {
        var foundUser = userRepository.findByEmail(dto.getEmail());
        if (foundUser.isEmpty()) {
            throw new EmailNotFoundException(UserConstants.EMAIL_NOT_FOUND);
        }

        final String token = Utilities.generateCode();

        foundUser.get()
                .setToken(token);

        userRepository.save(foundUser.get());


        emailService.sendMail(dto.getEmail(), AppConstants.SUBJECT_EMAIL_FORGOT_PASSWORD,AppConstants.TEXT_FORGOT_PASSWORD + token);

        return new ResponseSuccess<>(UserConstants.SEND_MAIL_FORGOT_PASSWORD_SUCCESS, true);
    }


    @Override
    @Transactional
    public ResponseSuccess<String> changeAvatar(ChangeAvatarDTO dto, User user) throws IOException {
        String avatarUrl = fileStorageService.uploadFile(dto.getAvatar());

        user.setAvatar(avatarUrl);
        var save = userRepository.save(user);

        return new ResponseSuccess<>(UserConstants.CHANGE_AVATAR_SUCCESS, save.getAvatar());
    }

    @Override
    public ResponseSuccess<Boolean> changePassword(ChangePasswordDTO dto, User user) {
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new PasswordIncorrectException(UserConstants.PASSWORD_INCORRECT);
        }
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new PasswordNotMatchException(UserConstants.PASSWORD_NOT_MATCH);
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));

        userRepository.save(user);

        return new ResponseSuccess<>(UserConstants.CHANGE_PASSWORD_SUCCESS, true);
    }
    @Override
    public ResponseSuccess<Boolean> forgotPassword(ForgotPasswordDTO dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new PasswordNotMatchException(UserConstants.PASSWORD_NOT_MATCH);
        }

        var foundUser = userRepository.findByEmailAndToken(dto.getEmail(),dto.getToken());
        if (foundUser.isEmpty()) {
            throw new InvalidTokenException(UserConstants.INVALID_CODE);
        }

        foundUser.get()
                .setPassword(passwordEncoder.encode(dto.getNewPassword()));
        foundUser.get()
                .setToken(null);

        userRepository.save(foundUser.get());

        return new ResponseSuccess<>(UserConstants.FORGOT_PASSWORD_SUCCESS, true);
    }

    @Override
    public ResponseSuccess<String> changeDisplayName(ChangeDisplayNameDTO dto, User user) {
        user.setDisplayName(dto.getDisplayName());
        var save = userRepository.save(user);

        return new ResponseSuccess<>(UserConstants.CHANGE_DISPLAY_NAME, save.getDisplayName());
    }



    @Override
    public ResponseSuccess<UserVm> getMe(User user) {
        return new ResponseSuccess<>("Thành công", Utilities.getUserVm(user));
    }




}
