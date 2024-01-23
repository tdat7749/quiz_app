package com.example.backend.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseError{
    private HttpStatus httpStatus;
    private Integer httpStatusCode;
    private String message;
}
