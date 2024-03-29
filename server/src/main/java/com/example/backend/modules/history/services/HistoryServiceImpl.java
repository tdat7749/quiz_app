package com.example.backend.modules.history.services;

import com.example.backend.commons.AppConstants;
import com.example.backend.commons.ResponsePaging;
import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.history.constant.HistoryConstants;
import com.example.backend.modules.history.dtos.CreateHistoryAnswerDTO;
import com.example.backend.modules.history.dtos.CreateHistoryDTO;
import com.example.backend.modules.history.models.History;
import com.example.backend.modules.history.models.HistoryAnswer;
import com.example.backend.modules.history.repositories.HistoryRepository;
import com.example.backend.modules.history.viewmodels.HistoryRoomVm;
import com.example.backend.modules.quiz.constant.QuizConstants;
import com.example.backend.modules.quiz.exceptions.QuizNotFoundException;
import com.example.backend.modules.quiz.models.Quiz;
import com.example.backend.modules.quiz.services.QuizService;
import com.example.backend.modules.room.constant.RoomConstants;
import com.example.backend.modules.room.exceptions.RoomNotFoundException;
import com.example.backend.modules.room.models.Room;
import com.example.backend.modules.room.services.RoomService;
import com.example.backend.modules.user.models.User;
import com.example.backend.utils.Utilities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HistoryServiceImpl implements HistoryService{

    private final QuizService quizService;
    private final RoomService roomService;

    private final HistoryRepository historyRepository;

    private final HistoryAnswerService historyAnswerService;

    public HistoryServiceImpl(
            QuizService quizService,
            RoomService roomService,
            HistoryRepository historyRepository,
            HistoryAnswerService historyAnswerService
    ){
        this.quizService = quizService;
        this.roomService = roomService;
        this.historyRepository = historyRepository;
        this.historyAnswerService = historyAnswerService;
    }

    @Override
    public Optional<History> findByIdAndUser(int id,User user) {
        return historyRepository.findByIdAndUser(id,user);
    }

    @Override
    @Transactional
    public ResponseSuccess<Boolean> createHistory(CreateHistoryDTO dto,User user) {
        Optional<Room> room = null;
        Optional<Quiz> quiz = null;
        if(dto.getRoomId() != null){
            room = roomService.findById(dto.getRoomId());
            if(room.isEmpty()){
                throw new RoomNotFoundException(RoomConstants.ROOM_NOT_FOUND);
            }
        }

        if(dto.getQuizId() != null){
            quiz = quizService.findById(dto.getQuizId());
            if(quiz.isEmpty()){
                throw new QuizNotFoundException(QuizConstants.QUIZ_NOT_FOUND);
            }
        }

        History history = History.builder()
                .room(room.get())
                .quiz(quiz.get())
                .score(dto.getTotalScore())
                .user(user)
                .createdAt(new Date())
                .finishedAt(dto.getFinishedAt())
                .startedAt(dto.getStartedAt())
                .totalCorrect(dto.getTotalCorrect())
                .updatedAt(new Date())
                .build();

        var saveHistory = historyRepository.save(history);

        historyAnswerService.createBulkHistoryAnswer(dto.getHistoryAnswers(),history);

        return new ResponseSuccess<>(HistoryConstants.CREATE_HISTORY,true);
    }

    @Override
    public ResponseSuccess<ResponsePaging<List<HistoryRoomVm>>> getHistoryRoom(int pageIndex, User user) {
        Pageable paging = PageRequest.of(pageIndex, AppConstants.PAGE_SIZE, Sort.by(Sort.Direction.DESC,"createdAt"));

        Page<History> pagingResult = historyRepository.getHistoryRoom(user,paging);

        var listHistoryRoomVm = pagingResult.stream().map(Utilities::getHistoryRoomVm).toList();

        ResponsePaging responsePaging = ResponsePaging.builder()
                .data(listHistoryRoomVm)
                .totalPage(pagingResult.getTotalPages())
                .totalRecord((int) pagingResult.getTotalElements())
                .build();

        return new ResponseSuccess<>("Thành công",responsePaging);
    }

    @Override
    public ResponseSuccess<ResponsePaging<List<HistoryRoomVm>>> getHistorySingle(int pageIndex, User user) {
        Pageable paging = PageRequest.of(pageIndex, AppConstants.PAGE_SIZE, Sort.by(Sort.Direction.DESC,"createdAt"));

        Page<History> pagingResult = historyRepository.getHistorySingle(user,paging);

        var listHistoryRoomVm = pagingResult.stream().map(Utilities::getHistorySingleVm).toList();

        ResponsePaging responsePaging = ResponsePaging.builder()
                .data(listHistoryRoomVm)
                .totalPage(pagingResult.getTotalPages())
                .totalRecord((int) pagingResult.getTotalElements())
                .build();

        return new ResponseSuccess<>("Thành công",responsePaging);
    }
}
