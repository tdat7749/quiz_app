package com.example.backend.modules.room.repositories;

import com.example.backend.modules.room.models.GameMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GameModeRepository extends JpaRepository<GameMode, Integer> {

}
