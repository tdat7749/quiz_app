package com.example.backend.modules.quiz.repositories;


import com.example.backend.modules.quiz.models.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer,Integer> {
    Optional<Answer> findById(int id);
}
