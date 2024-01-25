package com.example.backend.modules.quiz.services;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.quiz.dtos.CreateQuestionDTO;
import com.example.backend.modules.quiz.dtos.EditQuestionDTO;
import com.example.backend.modules.quiz.models.Question;
import com.example.backend.modules.quiz.repositories.QuestionRepository;
import com.example.backend.modules.quiz.viewmodels.QuestionDetailVm;
import com.example.backend.modules.user.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService{

    private final QuestionRepository questionRepository;

    public QuestionServiceImpl(
            QuestionRepository questionRepository
    ){
        this.questionRepository = questionRepository;
    }

    @Override
    public Optional<Question> findById(int id) {
        return questionRepository.findById(id);
    }

    @Override
    public Boolean createBulkQuestion(List<CreateQuestionDTO> listDto) {
        return null;
    }

    @Override
    public ResponseSuccess<Boolean> editQuestion(EditQuestionDTO dto) {
        return null;
    }

    @Override
    public Boolean isAuthor(User user, int quizId) {
        return null;
    }

    @Override
    public ResponseSuccess<Boolean> deleteQuestion(User user, int questionId) {
        return null;
    }

    @Override
    public ResponseSuccess<List<QuestionDetailVm>> getListQuestions(int quizId) {
        return null;
    }
}
