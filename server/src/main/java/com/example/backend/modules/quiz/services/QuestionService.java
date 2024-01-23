package com.example.backend.modules.quiz.services;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.quiz.dtos.CreateQuestionDTO;
import com.example.backend.modules.quiz.dtos.EditQuestionDTO;
import com.example.backend.modules.quiz.viewmodels.QuestionVm;
import com.example.backend.modules.user.models.User;

import java.util.List;

public interface QuestionService {
    Boolean createBulkQuestion(List<CreateQuestionDTO> listDto);

    ResponseSuccess<Boolean> editQuestion(EditQuestionDTO dto);

    Boolean isAuthor(User user, int quizId);

    ResponseSuccess<Boolean> deleteQuestion(User user, int questionId);

    ResponseSuccess<List<QuestionVm>> getListQuestions(int quizId);
}
