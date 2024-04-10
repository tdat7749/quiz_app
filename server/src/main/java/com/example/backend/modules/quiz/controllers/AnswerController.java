package com.example.backend.modules.quiz.controllers;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.quiz.dtos.EditAnswerDTO;
import com.example.backend.modules.quiz.dtos.EditQuestionDTO;
import com.example.backend.modules.quiz.services.AnswerService;
import com.example.backend.modules.quiz.viewmodels.AnswerVm;
import com.example.backend.modules.user.models.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/answers")
public class AnswerController {
    private final AnswerService answerService;

    public AnswerController(
            AnswerService answerService
    ){
        this.answerService = answerService;
    }

    @PatchMapping("/")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<List<AnswerVm>>> editAnswer(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid EditAnswerDTO dto
    ) throws IOException {
        var result = answerService.editAnswer(user,dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{answerId}/quiz/{quizId}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> deleteAnswer(
            @AuthenticationPrincipal User user,
            @PathVariable int answerId,
            @PathVariable int quizId
    ){
        var result = answerService.deleteAnswer(user,answerId,quizId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
