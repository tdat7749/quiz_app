package com.example.backend.modules.auth.services;


import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.auth.constant.AuthConstants;
import com.example.backend.modules.auth.dtos.LoginDTO;
import com.example.backend.modules.auth.dtos.RegisterDTO;
import com.example.backend.modules.auth.exceptions.EmailUsedException;
import com.example.backend.modules.auth.exceptions.UserNameUsedException;
import com.example.backend.modules.auth.viewmodels.AuthenVm;
import com.example.backend.modules.jwt.services.JwtService;
import com.example.backend.modules.user.Role;
import com.example.backend.modules.user.constant.UserConstant;
import com.example.backend.modules.user.exceptions.UserNotFoundException;
import com.example.backend.modules.user.models.User;
import com.example.backend.modules.user.services.UserService;
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

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserService userService,
            PasswordEncoder passwordEncoder
    ){
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userService = userService;
    }
    @Override
    public ResponseSuccess<AuthenVm> login(LoginDTO dto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                dto.getUserName(),
                dto.getPassword()));

        var user = userService.findByUserName(dto.getUserName());
        if(user.isEmpty()){
            throw new UserNotFoundException(UserConstant.USER_NOT_FOUND);
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
                .build();

        userService.saveUser(newUser);
        return new ResponseSuccess<>(AuthConstants.REGISTER_SUCCESS, true);
    }
}
