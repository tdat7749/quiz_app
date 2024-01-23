package com.example.backend.modules.quiz.services;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.quiz.dtos.CreateAnswerDTO;
import com.example.backend.modules.quiz.dtos.EditAnswerDTO;
import com.example.backend.modules.quiz.viewmodels.AnswerVm;
import com.example.backend.modules.user.models.User;

import java.util.List;

public interface AnswerService {
    boolean createBulkAnswer(List<CreateAnswerDTO> listDto);


    ResponseSuccess<AnswerVm> editAnswer(User user, EditAnswerDTO dto);

    ResponseSuccess<Boolean> deleteAnswer(User user, int answerId);
}
