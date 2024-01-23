package com.example.backend.modules.user.services;

import com.example.backend.modules.user.repositories.UserRepository;
import com.example.backend.modules.user.constant.UserConstant;
import com.example.backend.modules.user.exceptions.UserNotFoundException;
import com.example.backend.modules.user.models.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(
            UserRepository userRepository
    ){
        this.userRepository = userRepository;
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
    public void saveUser(User user) {
        userRepository.save(user);
    }
}
