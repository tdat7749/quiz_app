package com.example.backend.modules.quiz.services;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.quiz.dtos.CreateAnswerDTO;
import com.example.backend.modules.quiz.dtos.EditAnswerDTO;
import com.example.backend.modules.quiz.models.Answer;
import com.example.backend.modules.quiz.viewmodels.AnswerVm;
import com.example.backend.modules.user.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AnswerService {

    Optional<Answer> findById(int id);

    ResponseSuccess<AnswerVm> editAnswer(User user, EditAnswerDTO dto);

    ResponseSuccess<Boolean> deleteAnswer(User user, int answerId,int quizId);
}
