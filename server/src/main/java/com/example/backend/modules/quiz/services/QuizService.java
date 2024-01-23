package com.example.backend.modules.quiz.services;


import com.example.backend.modules.quiz.models.Quiz;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface QuizService {
    Optional<Quiz> findById(int id);
    Optional<Quiz> findBySlug(String slug);
}
