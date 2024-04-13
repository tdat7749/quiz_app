package com.example.backend.modules.room.viewmodels;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JoinRoomVm {
    private int id;
    private GameModeVm mode;
}
