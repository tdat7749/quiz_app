package com.example.backend.modules.history.repositories;


import com.example.backend.modules.history.models.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository  extends JpaRepository<History,Integer> {
}
