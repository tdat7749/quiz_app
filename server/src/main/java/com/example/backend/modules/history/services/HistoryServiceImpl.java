package com.example.backend.modules.history.services;

import com.example.backend.commons.AppConstants;
import com.example.backend.commons.ResponsePaging;
import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.history.constant.HistoryConstants;
import com.example.backend.modules.history.dtos.CreateHistoryDTO;
import com.example.backend.modules.history.exceptions.CannotPlayAgainException;
import com.example.backend.modules.history.models.History;
import com.example.backend.modules.history.repositories.HistoryRepository;
import com.example.backend.modules.history.viewmodels.*;
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
        Optional<Room> room = Optional.empty();
        Optional<Quiz> quiz = Optional.empty();
        if(dto.getRoomId() != null){
            room = roomService.findById(dto.getRoomId());
            if(room.isEmpty()){
                throw new RoomNotFoundException(RoomConstants.ROOM_NOT_FOUND);
            }

            var foundHistory = historyRepository.findByUserAndRoom(user,room.get());
            if(!room.get().isPlayAgain()){
                if(foundHistory.isPresent()){
                    throw new CannotPlayAgainException(HistoryConstants.CANNOT_PLAY_AGAIN);
                }
            }
            if(foundHistory.isPresent()){
                historyAnswerService.deleteByHistoryId(foundHistory.get().getId());
                historyRepository.delete(foundHistory.get());
            }

            History history = History.builder()
                    .room(room.get())
                    .quiz(null)
                    .score(dto.getTotalScore())
                    .user(user)
                    .createdAt(new Date())
                    .finishedAt(dto.getFinishedAt())
                    .startedAt(dto.getStartedAt())
                    .totalCorrect(dto.getTotalCorrect())
                    .updatedAt(new Date())
                    .build();

            var saveHistory = historyRepository.save(history);

            historyAnswerService.createBulkHistoryAnswer(dto.getHistoryAnswers(),saveHistory);
        }

        if(dto.getQuizId() != null){
            quiz = quizService.findById(dto.getQuizId());
            if(quiz.isEmpty()){
                throw new QuizNotFoundException(QuizConstants.QUIZ_NOT_FOUND);
            }

            var foundHistory = historyRepository.findByUserAndQuiz(user,quiz.get());
            if(foundHistory.isPresent()){
                historyAnswerService.deleteByHistoryId(foundHistory.get().getId());
                historyRepository.delete(foundHistory.get());
            }

            History history = History.builder()
                    .room(null)
                    .quiz(quiz.orElse(null))
                    .score(dto.getTotalScore())
                    .user(user)
                    .createdAt(new Date())
                    .finishedAt(dto.getFinishedAt())
                    .startedAt(dto.getStartedAt())
                    .totalCorrect(dto.getTotalCorrect())
                    .updatedAt(new Date())
                    .build();

            var saveHistory = historyRepository.save(history);

            historyAnswerService.createBulkHistoryAnswer(dto.getHistoryAnswers(),saveHistory);
        }

        return new ResponseSuccess<>(HistoryConstants.CREATE_HISTORY,true);
    }

    @Override
    public ResponseSuccess<ResponsePaging<List<HistoryRankVm>>> getHistoryRankSingle(int quizId, int pageIndex) {
        Pageable paging = PageRequest.of(pageIndex, AppConstants.PAGE_SIZE, Sort.by(Sort.Direction.DESC,"score"));

        Page<HistoryRank> pagingResult = historyRepository.getHistoryRankSingle(quizId,paging);

        var listHistoryRoomVm = pagingResult.stream().map(Utilities::getHistoryRankVm).toList();

        ResponsePaging responsePaging = ResponsePaging.builder()
                .data(listHistoryRoomVm)
                .totalPage(pagingResult.getTotalPages())
                .totalRecord((int) pagingResult.getTotalElements())
                .build();

        return new ResponseSuccess<>("Thành công",responsePaging);
    }

    @Override
    public ResponseSuccess<ResponsePaging<List<HistoryRankVm>>> getHistoryRankRoom(int roomid, int pageIndex) {
        Pageable paging = PageRequest.of(pageIndex, AppConstants.PAGE_SIZE, Sort.by(Sort.Direction.DESC,"score"));

        Page<HistoryRank> pagingResult = historyRepository.getHistoryRankRoom(roomid,paging);

        var listHistoryRoomVm = pagingResult.stream().map(Utilities::getHistoryRankVm).toList();

        ResponsePaging responsePaging = ResponsePaging.builder()
                .data(listHistoryRoomVm)
                .totalPage(pagingResult.getTotalPages())
                .totalRecord((int) pagingResult.getTotalElements())
                .build();

        return new ResponseSuccess<>("Thành công",responsePaging);
    }

    @Override
    public ResponseSuccess<List<HistoryAnswerVm>> getListHistoryAnswer(User user, int roomId) {
        var foundedRoom = roomService.findById(roomId);
        if(foundedRoom.isEmpty()){
            throw new RoomNotFoundException(RoomConstants.ROOM_NOT_FOUND);
        }

        var foundedHistory = historyRepository.findByUserAndRoom(user,foundedRoom.get());
        if (foundedHistory.isEmpty()){
            return new ResponseSuccess<>("Thành công", new ArrayList<>());
        }

        var listHistoryAnswer = foundedHistory.get().getHistoryAnswers();

        var vm = listHistoryAnswer.stream().map(Utilities::getHistoryAnswer).toList();
        return new ResponseSuccess<>("Thành công", vm);
    }
}
