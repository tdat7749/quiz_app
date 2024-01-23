package com.example.backend.modules.quiz.services;


import com.example.backend.commons.ResponsePaging;
import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.quiz.dtos.CreateQuizDTO;
import com.example.backend.modules.quiz.dtos.EditQuizDTO;
import com.example.backend.modules.quiz.models.Quiz;
import com.example.backend.modules.quiz.viewmodels.QuizDetailVm;
import com.example.backend.modules.quiz.viewmodels.QuizVm;
import com.example.backend.modules.user.models.User;
import org.apache.coyote.Response;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface QuizService {
    Optional<Quiz> findById(int id);
    Optional<Quiz> findBySlug(String slug);

    ResponseSuccess<ResponsePaging<List<QuizVm>>> getMyListQuizzes(String keyword,String sortBy,int pageIndex, User user);

    ResponseSuccess<ResponsePaging<List<QuizVm>>> getMyListCollection(String keyword,String sortBy,int pageIndex,User user);

    ResponseSuccess<ResponsePaging<List<QuizVm>>> getListPublicQuizzes(String keyword,String sortBy,int pageIndex,int topicId, User user);

    ResponseSuccess<QuizDetailVm> getQuizDetail(int quizId);

    ResponseSuccess<QuizVm> createQuiz(User user, CreateQuizDTO dto);

    ResponseSuccess<QuizVm> editQuiz(User user, EditQuizDTO dto);

    ResponseSuccess<Boolean> deleteQuiz(User user, int quizId);

    ResponseSuccess<String> editThumbnail(User user, String thumbnail);



}
