package com.example.backend.modules.quiz.controllers;


import com.example.backend.commons.AppConstants;
import com.example.backend.commons.ResponsePaging;
import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.quiz.dtos.CreateQuizDTO;
import com.example.backend.modules.quiz.dtos.EditQuizDTO;
import com.example.backend.modules.quiz.dtos.EditQuizThumbnail;
import com.example.backend.modules.quiz.services.QuizService;
import com.example.backend.modules.quiz.viewmodels.QuizDetailVm;
import com.example.backend.modules.quiz.viewmodels.QuizVm;
import com.example.backend.modules.user.models.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/quizzes")
public class QuizController {
    private final QuizService quizService;

    public QuizController(
            QuizService quizService
    ){
        this.quizService = quizService;
    }

    @GetMapping("/my")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<ResponsePaging<List<QuizVm>>>> getMyListQuizzes(
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "pageIndex", required = true, defaultValue = "0") Integer pageIndex,
            @RequestParam(name = "sortBy", required = false, defaultValue = AppConstants.SORT_BY_CREATED_AT) String sortBy,
            @AuthenticationPrincipal User user
    ) {
        var result = quizService.getMyListQuizzes(keyword,sortBy,pageIndex,user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/public/{topicId}/topic")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<ResponsePaging<List<QuizVm>>>> getListPublicQuizzes(
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "pageIndex", required = true, defaultValue = "0") Integer pageIndex,
            @RequestParam(name = "sortBy", required = false, defaultValue = AppConstants.SORT_BY_CREATED_AT) String sortBy,
            @PathVariable int topicId
    ) {
        var result = quizService.getListPublicQuizzes(keyword,sortBy,pageIndex,topicId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{quizId}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<QuizDetailVm>> getQuizDetail(
            @PathVariable int quizId
    ) {
        var result = quizService.getQuizDetail(quizId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<QuizVm>> createQuiz(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CreateQuizDTO dto
            ) throws IOException {
        var result = quizService.createQuiz(user,dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PatchMapping("/")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<QuizVm>> editQuiz(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid EditQuizDTO dto
    ) throws IOException {
        var result = quizService.editQuiz(user,dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/thumbnail")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<String>> editThumbnail(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid EditQuizThumbnail dto
    ) throws IOException {
        var result = quizService.editThumbnail(user,dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{quizId}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> editQuiz(
            @AuthenticationPrincipal User user,
            @PathVariable int quizId
    ) throws IOException {
        var result = quizService.deleteQuiz(user,quizId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
