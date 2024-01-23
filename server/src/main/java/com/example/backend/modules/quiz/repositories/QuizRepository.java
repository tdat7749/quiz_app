package com.example.backend.modules.quiz.repositories;


import com.example.backend.modules.quiz.models.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz,Integer> {
    Optional<Quiz> findById(int id);

    Optional<Quiz> findBySlug(String slug);
}
