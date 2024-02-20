package com.example.backend.modules.quiz.controllers;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.quiz.dtos.EditQuestionDTO;
import com.example.backend.modules.quiz.services.QuestionService;
import com.example.backend.modules.quiz.viewmodels.QuestionDetailVm;
import com.example.backend.modules.user.models.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/questions")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(
            QuestionService questionService
    ){
        this.questionService = questionService;
    }

    @PatchMapping("/")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> editQuestion(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid EditQuestionDTO dto
    ) throws IOException {
        var result = questionService.editQuestion(user,dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{questionId}/quiz/{quizId}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> deleteQuestion(
            @AuthenticationPrincipal User user,
            @PathVariable int questionId,
            @PathVariable int quizId
    ){
        var result = questionService.deleteQuestion(user,questionId,quizId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{quizId}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<List<QuestionDetailVm>>> getListQuestions(
            @AuthenticationPrincipal User user,
            @PathVariable int quizId
    ){
        var result = questionService.getListQuestions(user,quizId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
