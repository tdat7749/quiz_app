package com.example.backend.modules.room.services;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.room.models.GameMode;
import com.example.backend.modules.room.repositories.GameModeRepository;
import com.example.backend.modules.room.viewmodels.GameModeVm;
import com.example.backend.utils.Utilities;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class GameModeServiceImpl implements GameModeService{
    private final GameModeRepository gameModeRepository;

    public GameModeServiceImpl(GameModeRepository gameModeRepository){
        this.gameModeRepository = gameModeRepository;
    }
    @Override
    public Optional<GameMode> findById(int id) {
        return gameModeRepository.findById(id);
    }

    @Override
    public ResponseSuccess<List<GameModeVm>> getListGameMode() {
        var listGameMode = gameModeRepository.findAll();
        List<GameModeVm> vm = listGameMode.stream().map(Utilities::getGameModeVm).toList();

        return new ResponseSuccess<>("Thành công",vm);
    }
}
