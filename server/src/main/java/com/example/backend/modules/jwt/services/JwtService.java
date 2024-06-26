package com.example.backend.modules.jwt.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public interface JwtService {

    public String generateAccessToken(UserDetails user);
    public String extractUsername(String token);

    public String buildToken(Map<String,Object> extraClaims, UserDetails user, long tokenTime);

    public String generateRefreshToken(UserDetails user);

    public boolean isTokenValid(String token,UserDetails user);
}
