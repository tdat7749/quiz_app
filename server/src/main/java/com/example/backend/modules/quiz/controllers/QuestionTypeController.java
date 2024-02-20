package com.example.backend.modules.quiz.controllers;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.quiz.services.QuestionTypeService;
import com.example.backend.modules.quiz.viewmodels.QuestionTypeVm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/types")
public class QuestionTypeController {
    private final QuestionTypeService questionTypeService;

    public QuestionTypeController(
            QuestionTypeService questionTypeService
    ){
        this.questionTypeService = questionTypeService;
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<List<QuestionTypeVm>>> getListQuestionType() {
        var result = questionTypeService.getListQuestionType();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
