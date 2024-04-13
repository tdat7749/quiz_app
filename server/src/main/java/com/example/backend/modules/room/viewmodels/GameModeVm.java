package com.example.backend.modules.room.viewmodels;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameModeVm {
    private int id;
    private String modeName;
    private String modeCode;
}
