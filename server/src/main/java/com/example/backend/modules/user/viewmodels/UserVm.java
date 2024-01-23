package com.example.backend.modules.user.viewmodels;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserVm {
    private int id;
    private String displayName;
    private String avatar;
}
