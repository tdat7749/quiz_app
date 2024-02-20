package com.example.backend.modules.quiz.services;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.quiz.models.QuestionType;
import com.example.backend.modules.quiz.viewmodels.QuestionTypeVm;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface QuestionTypeService {
    ResponseSuccess<List<QuestionTypeVm>> getListQuestionType();

    Optional<QuestionType> findById(int id);
}
