package com.example.backend.modules.history.repositories;


import com.example.backend.modules.history.models.HistoryAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryAnswerRepository extends JpaRepository<HistoryAnswer,Integer> {
}
