package com.example.backend.commons;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponsePaging <T>{
    private int totalPage;
    private int totalRecord;
    private T data;
}
