package com.example.backend.modules.history.controllers;

import com.example.backend.commons.ResponsePaging;
import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.history.dtos.CreateHistoryDTO;
import com.example.backend.modules.history.services.HistoryAnswerService;
import com.example.backend.modules.history.services.HistoryService;
import com.example.backend.modules.history.viewmodels.HistoryAnswerVm;
import com.example.backend.modules.history.viewmodels.HistoryRankVm;
import com.example.backend.modules.history.viewmodels.HistoryRoomVm;
import com.example.backend.modules.history.viewmodels.HistorySingleVm;
import com.example.backend.modules.user.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/histories") // http://localhost:8080/api/histories
public class HistoryController {
    private final HistoryService historyService;
    private final HistoryAnswerService historyAnswerService;

    public HistoryController(
            HistoryService historyService,
            HistoryAnswerService historyAnswerService
    ){
        this.historyAnswerService = historyAnswerService;
        this.historyService = historyService;
    }

    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> createHistory(
            @RequestBody CreateHistoryDTO dto,
            @AuthenticationPrincipal User user
    ) {
        var result = historyService.createHistory(dto,user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/single/{quizId}/rank")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<ResponsePaging<List<HistoryRankVm>>>> getHistoryRankSingle(
            @RequestParam(name = "pageIndex", required = true, defaultValue = "0") Integer pageIndex,
            @PathVariable int quizId
    ) {
        var result = historyService.getHistoryRankSingle(quizId,pageIndex);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/room/{roomId}/rank")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<ResponsePaging<List<HistoryRankVm>>>> getHistoryRankRoom(
            @RequestParam(name = "pageIndex", required = true, defaultValue = "0") Integer pageIndex,
            @PathVariable int roomId
    ) {
        var result = historyService.getHistoryRankRoom(roomId,pageIndex);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{historyId}/answer")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<List<HistoryAnswerVm>>> getListHistoryAnswer(
            @PathVariable("historyId") int historyId,
            @AuthenticationPrincipal User user
    ) {
        var result = historyAnswerService.getListHistoryAnswer(user,historyId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
