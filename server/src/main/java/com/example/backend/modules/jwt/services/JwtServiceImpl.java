package com.example.backend.modules.jwt.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtServiceImpl implements JwtService{
    private final String serectKey = "qu!zzappcodebyspringbootandkotlinjetpackcompose";
    private final long accessTokenTime =  1000 * 60 * 60 * 24; // 1 day'
    private final long refreshTokenTime = 1000 * 60 * 60 * 24 * 7; // 7 days



    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String generateAccessToken(UserDetails user){
        return buildToken(new HashMap<>(),user,accessTokenTime);
    }

    @Override
    public String generateRefreshToken(UserDetails user) {
        return buildToken(new HashMap<>(),user,refreshTokenTime);
    }



    @Override
    public String buildToken(Map<String, Object> extraClaims, UserDetails user,long tokenTime) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token,UserDetails user){
        String userName = extractUsername(token);

        return ((userName.equals(user.getUsername())) && !isTokenExpired(token));
    }

    private Claims extractClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    private  boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }



    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(serectKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
