package com.example.backend.modules.jwt.exceptions;


import com.example.backend.commons.ResponseError;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.InvalidKeyException;
import java.security.SignatureException;

@RestControllerAdvice
public class JwtExceptionHandler {
    @ExceptionHandler(InvalidKeyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError invalidKeyExceptionHandler(InvalidKeyException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,"Key không hợp lệ");
    }

    @ExceptionHandler(DecodingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError decodingExceptionHandler(DecodingException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,"Decoding lỗi");
    }


    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseError expiredJwtExceptionHandler(ExpiredJwtException ex){
        return new ResponseError(HttpStatus.UNAUTHORIZED,401,"Phiên đăng nhập hết hạn");
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError unsupportedJwtExceptionHandler(UnsupportedJwtException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,"Token có định dạng không đúng");
    }

    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError malformedJwtExceptionHandler(MalformedJwtException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,"Token có cấu trúc không phù hợp");
    }

    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError signatureExceptionHandler(SignatureException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,"Chữ ký đi cùng không hợp lệ");
    }


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError illegalArgumentExceptionHandler(IllegalArgumentException ex){
        return new ResponseError(HttpStatus.BAD_REQUEST,400,ex.getMessage());
    }
}
