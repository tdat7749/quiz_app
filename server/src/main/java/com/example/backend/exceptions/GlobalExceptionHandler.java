package com.example.backend.exceptions;

import com.example.backend.commons.ResponseError;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError methodArgumentNotValidHandler(MethodArgumentNotValidException ex) {
        String defaultMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        return new ResponseError(HttpStatus.BAD_REQUEST, 400, defaultMessage);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseError accessDeniedExceptionHandler(AccessDeniedException ex) {
        return new ResponseError(HttpStatus.UNAUTHORIZED, 401, "Không có quyền truy cập");
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseError expiredJwtExceptionHandler(ExpiredJwtException ex) {
        return new ResponseError(HttpStatus.UNAUTHORIZED, 401, "Phiên đăng nhập hết hạn");
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError authenticationExceptionHandler(AuthenticationException ex) {
        return new ResponseError(HttpStatus.BAD_REQUEST, 400, "Sai tài khoản hoặc mật khẩu");
    }

    @ExceptionHandler(JsonProcessingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseError jsonProcessingExceptionHandler(JsonProcessingException ex) {
        return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR, 500, "Lỗi xảy ra, vui lòng thử lại sau");
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseError sqlIntegrityConstraintViolationExceptionHandler(SQLIntegrityConstraintViolationException ex) {
        return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR, 500, "Lỗi xảy ra, vui lòng thử lại sau");
    }

    @ExceptionHandler(LockedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError lockedExceptionHandler(LockedException ex) {
        return new ResponseError(HttpStatus.BAD_REQUEST, 400,
                "Tài khoản đã bị khóa, vui lòng liên hệ admin nếu có nhầm lẫn gì đó");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError dataIntegrityViolationExceptionHandler(DataIntegrityViolationException ex) {
        return new ResponseError(HttpStatus.BAD_REQUEST, 400,
                "Lỗi xảy ra, vui lòng thử lại sau");
    }
}