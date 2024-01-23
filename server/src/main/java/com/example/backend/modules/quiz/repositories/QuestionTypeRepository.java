package com.example.backend.modules.quiz.repositories;

import com.example.backend.modules.quiz.models.QuestionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface QuestionTypeRepository extends JpaRepository<QuestionType,Integer> {
}
