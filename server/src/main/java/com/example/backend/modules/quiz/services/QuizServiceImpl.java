package com.example.backend.modules.quiz.services;


import com.example.backend.commons.ResponsePaging;
import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.quiz.dtos.CreateQuizDTO;
import com.example.backend.modules.quiz.dtos.EditQuizDTO;
import com.example.backend.modules.quiz.models.Quiz;
import com.example.backend.modules.quiz.repositories.QuizRepository;
import com.example.backend.modules.quiz.viewmodels.QuizDetailVm;
import com.example.backend.modules.quiz.viewmodels.QuizVm;
import com.example.backend.modules.user.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public ResponseSuccess<ResponsePaging<List<QuizVm>>> getMyListQuizzes(String keyword, String sortBy, int pageIndex, User user) {
        return null;
    }

    @Override
    public ResponseSuccess<ResponsePaging<List<QuizVm>>> getMyListCollection(String keyword, String sortBy, int pageIndex, User user) {
        return null;
    }

    @Override
    public ResponseSuccess<ResponsePaging<List<QuizVm>>> getListPublicQuizzes(String keyword, String sortBy, int pageIndex, int topicId, User user) {
        return null;
    }

    @Override
    public ResponseSuccess<QuizDetailVm> getQuizDetail(int quizId) {
        return null;
    }

    @Override
    public ResponseSuccess<QuizVm> createQuiz(User user, CreateQuizDTO dto) {
        return null;
    }

    @Override
    public ResponseSuccess<QuizVm> editQuiz(User user, EditQuizDTO dto) {
        return null;
    }

    @Override
    public ResponseSuccess<Boolean> deleteQuiz(User user, int quizId) {
        return null;
    }

    @Override
    public ResponseSuccess<String> editThumbnail(User user, String thumbnail) {
        return null;
    }
}
