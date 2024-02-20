package com.example.backend.modules.quiz.services;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.quiz.models.QuestionType;
import com.example.backend.modules.quiz.repositories.QuestionTypeRepository;
import com.example.backend.modules.quiz.viewmodels.QuestionTypeVm;
import com.example.backend.utils.Utilities;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class QuestionTypeServiceImpl implements QuestionTypeService{

    private final QuestionTypeRepository questionTypeRepository;

    public QuestionTypeServiceImpl(
            QuestionTypeRepository questionTypeRepository
    ){
        this.questionTypeRepository = questionTypeRepository;
    }

    @Override
    public ResponseSuccess<List<QuestionTypeVm>> getListQuestionType() {
        var listQuestionType = questionTypeRepository.findAll();

        var result = listQuestionType.stream().map(Utilities::getQuestionTypeVm).toList();

        return new ResponseSuccess<>("Thành công",result);
    }

    @Override
    public Optional<QuestionType> findById(int id) {
        return questionTypeRepository.findById(id);
    }
}
