package com.example.backend.modules.quiz.services;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.quiz.dtos.CreateQuestionDTO;
import com.example.backend.modules.quiz.dtos.EditQuestionDTO;
import com.example.backend.modules.quiz.models.Question;
import com.example.backend.modules.quiz.models.Quiz;
import com.example.backend.modules.quiz.viewmodels.QuestionDetailVm;
import com.example.backend.modules.user.models.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
public interface QuestionService {

    Optional<Question> findById(int id);

    Boolean createBulkQuestion(List<CreateQuestionDTO> listDto, Quiz quiz) throws IOException;

    ResponseSuccess<Boolean> editQuestion(User user,EditQuestionDTO dto) throws IOException;


    ResponseSuccess<Boolean> deleteQuestion(User user, int questionId,int quizId);

    ResponseSuccess<List<QuestionDetailVm>> getListQuestions(User user,int quizId);
}
