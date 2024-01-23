package com.example.backend.modules.quiz.services;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.quiz.viewmodels.QuestionTypeVm;

public interface QuestionTypeService {
    ResponseSuccess<QuestionTypeVm> getListQuestionType();
}
