package com.example.backend.commons;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
public class ResponsePaging <T>{
    private int totalPage;
    private int totalRecord;
    private T data;
}
