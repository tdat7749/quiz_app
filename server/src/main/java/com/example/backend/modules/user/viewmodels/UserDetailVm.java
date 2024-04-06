package com.example.backend.modules.user.viewmodels;

import com.example.backend.modules.user.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetailVm {
    private int id;
    private String displayName;
    private String avatar;
    private String email;
    private String userName;
    private Role role;
}
