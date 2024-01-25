package com.example.backend.modules.quiz.services;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.quiz.dtos.CreateQuestionDTO;
import com.example.backend.modules.quiz.dtos.EditQuestionDTO;
import com.example.backend.modules.quiz.models.Question;
import com.example.backend.modules.quiz.viewmodels.QuestionDetailVm;
import com.example.backend.modules.user.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface QuestionService {

    Optional<Question> findById(int id);

    Boolean createBulkQuestion(List<CreateQuestionDTO> listDto);

    ResponseSuccess<Boolean> editQuestion(EditQuestionDTO dto);

    Boolean isAuthor(User user, int quizId);

    ResponseSuccess<Boolean> deleteQuestion(User user, int questionId);

    ResponseSuccess<List<QuestionDetailVm>> getListQuestions(int quizId);
}
