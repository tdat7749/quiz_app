package com.example.backend.modules.quiz.services;


import com.example.backend.modules.quiz.models.Quiz;
import com.example.backend.modules.quiz.repositories.QuizRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuizServiceImpl implements QuizService{
    private final QuizRepository quizRepository;

    public QuizServiceImpl(QuizRepository quizRepository){
        this.quizRepository = quizRepository;
    }

    @Override
    public Optional<Quiz> findById(int id) {
        return quizRepository.findById(id);
    }

    @Override
    public Optional<Quiz> findBySlug(String slug) {
        return quizRepository.findBySlug(slug);
    }
}
