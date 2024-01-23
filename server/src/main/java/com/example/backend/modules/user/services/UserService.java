package com.example.backend.modules.user.services;

import com.example.backend.modules.user.models.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    Optional<User> findByUserName(String userName);

    Optional<User> findByEmail(String email);

    void saveUser(User user);
}
