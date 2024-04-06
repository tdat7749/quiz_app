package com.example.backend.modules.quiz.controllers;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.quiz.dtos.CreateQuestionDTO;
import com.example.backend.modules.quiz.dtos.EditQuestionDTO;
import com.example.backend.modules.quiz.dtos.EditQuestionThumbnailDTO;
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
    public ResponseEntity<ResponseSuccess<QuestionDetailVm>> editQuestion(
            @AuthenticationPrincipal User user,
            @ModelAttribute @Valid EditQuestionDTO dto
    ) throws IOException {
        var result = questionService.editQuestion(user,dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/thumbnail")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<String>> editQuestionThumbnail(
            @AuthenticationPrincipal User user,
            @ModelAttribute @Valid EditQuestionThumbnailDTO dto
    ) throws IOException {
        var result = questionService.editQuestionThumbnail(user,dto);

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

    @PostMapping("/{quizId}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<QuestionDetailVm>> createQuestion(
            @ModelAttribute @Valid CreateQuestionDTO dto,
            @AuthenticationPrincipal User user,
            @PathVariable int quizId
    ) throws IOException {
        var result = questionService.createQuestion(user,dto,quizId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
