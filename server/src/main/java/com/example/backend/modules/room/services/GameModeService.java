package com.example.backend.modules.room.services;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.room.models.GameMode;
import com.example.backend.modules.room.viewmodels.GameModeVm;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface GameModeService {
    Optional<GameMode> findById(int id);
    ResponseSuccess<List<GameModeVm>> getListGameMode();
}
