package com.example.backend.modules.auth.services;


import com.example.backend.commons.AppConstants;
import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.auth.constant.AuthConstants;
import com.example.backend.modules.auth.dtos.LoginDTO;
import com.example.backend.modules.auth.dtos.RegisterDTO;
import com.example.backend.modules.auth.dtos.ResendMailDTO;
import com.example.backend.modules.auth.dtos.VerifyDTO;
import com.example.backend.modules.auth.exceptions.EmailUsedException;
import com.example.backend.modules.auth.exceptions.UserNameUsedException;
import com.example.backend.modules.auth.exceptions.VerifyEmailException;
import com.example.backend.modules.auth.viewmodels.AuthenVm;
import com.example.backend.modules.email.services.EmailService;
import com.example.backend.modules.jwt.services.JwtService;
import com.example.backend.modules.user.Role;
import com.example.backend.modules.user.constant.UserConstants;
import com.example.backend.modules.user.exceptions.UserNotFoundException;
import com.example.backend.modules.user.models.User;
import com.example.backend.modules.user.services.UserService;
import com.example.backend.utils.Utilities;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthServiceImpl  implements AuthService{
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserService userService,
            PasswordEncoder passwordEncoder,
            EmailService emailService
    ){
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userService = userService;
        this.emailService = emailService;
    }
    @Override
    public ResponseSuccess<AuthenVm> login(LoginDTO dto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                dto.getUserName(),
                dto.getPassword()));

        var user = userService.findByUserName(dto.getUserName());
        if(user.isEmpty()){
            throw new UserNotFoundException(UserConstants.USER_NOT_FOUND);
        }


        AuthenVm authenVm = AuthenVm.builder()
                .accessToken(jwtService.generateAccessToken(user.get()))
                .refreshToken(jwtService.generateRefreshToken(user.get()))
                .build();

        return new ResponseSuccess<>(AuthConstants.LOGIN_SUCCESS, authenVm);
    }

    @Override
    public ResponseSuccess<Boolean> register(RegisterDTO dto) {
        var isFoundUserByUserName = userService.findByUserName(dto.getUserName().toLowerCase());
        if(isFoundUserByUserName.isPresent()){
            throw new UserNameUsedException(AuthConstants.USERNAME_USED);
        }

        var isFoundUserByEmail = userService.findByEmail(dto.getEmail().toLowerCase());
        if(isFoundUserByEmail.isPresent()){
            throw new EmailUsedException(AuthConstants.EMAIL_USED);
        }

        final String verifyToken = Utilities.generateCode();

        User newUser = User.builder()
                .avatar("")
                .userName(dto.getUserName())
                .displayName(dto.getDisplayName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .role(Role.FREE)
                .createdAt(new Date())
                .updatedAt(new Date())
                .isNotLocked(true)
                .isEnable(false)
                .token(verifyToken)
                .build();

        userService.saveUser(newUser);

        emailService.sendMail(dto.getEmail(), AppConstants.SUBJECT_EMAIL,AppConstants.TEXT_VERIFY_EMAIL + verifyToken);

        return new ResponseSuccess<>(AuthConstants.REGISTER_SUCCESS, true);
    }

    @Override
    public ResponseSuccess<Boolean> verifyAccount(VerifyDTO dto) {
        var foundUser = userService.findByEmailAndToken(dto.getEmail().toLowerCase(), dto.getToken());
        if (foundUser.isEmpty()) {
            throw new VerifyEmailException(AuthConstants.VERIFY_FAILED);
        }
        if (foundUser.get().isEnabled()) {
            return new ResponseSuccess<>(AuthConstants.ACCOUNT_VERIFIED, true);
        }

        foundUser.get()
                .setIsEnable(true);
        foundUser.get()
                .setToken(null);

        userService.saveUser(foundUser.get());

        return new ResponseSuccess<>(AuthConstants.VERIFY_SUCCESS, true);
    }

    @Override
    public ResponseSuccess<Boolean> resendEmail(ResendMailDTO dto) {
        var foundUser = userService.findByEmail(dto.getEmail());
        if (foundUser.isEmpty()) {
            throw new UserNotFoundException(UserConstants.USER_NOT_FOUND);
        }

        if (foundUser.get().isEnabled()) {
            return new ResponseSuccess<>(AuthConstants.ACCOUNT_VERIFIED, true);
        }

        final String token = Utilities.generateCode();

        foundUser.get()
                .setToken(token);

        userService.saveUser(foundUser.get());

        emailService.sendMail(dto.getEmail(), AppConstants.SUBJECT_EMAIL,AppConstants.TEXT_VERIFY_EMAIL + token);


        return new ResponseSuccess<>(AuthConstants.RESEND_EMAIL, true);
    }

    @Override
    public ResponseSuccess<String> refreshToken(String refreshToken) {
        String userName = jwtService.extractUsername(refreshToken);
        if (userName != null) {
            var userFound = userService.findByUserName(userName);
            if (userFound.isEmpty()) {
                throw new UserNotFoundException(UserConstants.USER_NOT_FOUND);
            }

            var accessToken = jwtService.generateAccessToken(userFound.get());
            return new ResponseSuccess<>("Thành công", accessToken);
        }

        throw new UserNotFoundException(UserConstants.USER_NOT_FOUND);
    }
}
