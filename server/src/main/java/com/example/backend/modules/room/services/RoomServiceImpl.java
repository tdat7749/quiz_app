package com.example.backend.modules.room.services;

import com.example.backend.commons.AppConstants;
import com.example.backend.commons.ResponsePaging;
import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.quiz.constant.QuizConstants;
import com.example.backend.modules.quiz.exceptions.QuizNotFoundException;
import com.example.backend.modules.quiz.services.QuizService;
import com.example.backend.modules.room.constant.RoomConstants;
import com.example.backend.modules.room.dtos.CreateRoomDTO;
import com.example.backend.modules.room.dtos.EditRoomDTO;
import com.example.backend.modules.room.exceptions.RoomClosedException;
import com.example.backend.modules.room.exceptions.RoomNotFoundException;
import com.example.backend.modules.room.exceptions.RoomOwnerException;
import com.example.backend.modules.room.models.Room;
import com.example.backend.modules.room.repositories.RoomRepository;
import com.example.backend.modules.room.viewmodels.RoomVm;
import com.example.backend.modules.user.models.User;
import com.example.backend.utils.Utilities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class RoomServiceImpl implements RoomService{
    private final QuizService quizService;
    private final RoomRepository roomRepository;

    public RoomServiceImpl(
            QuizService quizService,
            RoomRepository roomRepository
    ){
        this.quizService = quizService;
        this.roomRepository = roomRepository;
    }

    @Override
    public Optional<Room> findById(int id) {

        return roomRepository.findById(id);
    }
    

    @Override
    public ResponseSuccess<RoomVm> createRoom(User user, CreateRoomDTO dto) {
        var quiz = quizService.findById(dto.getQuizId());
        if(quiz.isEmpty()){
            throw new QuizNotFoundException(QuizConstants.QUIZ_NOT_FOUND);
        }

        var newRoom = Room.builder()
                .createdAt(new Date())
                .timeStart(dto.getTimeStart() != null ? dto.getTimeStart() : null)
                .timeEnd(dto.getTimeEnd() != null ? dto.getTimeEnd() : null)
                .quiz(quiz.get())
                .user(user)
                .roomPin(Utilities.generateCode())
                .isClosed(false)
                .build();

        var save = roomRepository.save(newRoom);
        RoomVm roomVm = Utilities.getRoomVm(save);

        return new ResponseSuccess<>(RoomConstants.CREATE_ROOM_SUCCESS,roomVm);
    }

    @Override
    public ResponseSuccess<ResponsePaging<List<RoomVm>>> getMyListRooms(String keyword, String sortBy, int pageIndex,User user) {
        Pageable paging = PageRequest.of(pageIndex, AppConstants.PAGE_SIZE, Sort.by(Sort.Direction.DESC,sortBy));

        Page<Room> pagingResult = roomRepository.getMyListRooms(user,paging);
        List<RoomVm> roomVmList = pagingResult.stream().map(Utilities::getRoomVm).toList();

        ResponsePaging result = ResponsePaging.builder()
                .data(roomVmList)
                .totalPage(pagingResult.getTotalPages())
                .totalRecord((int) pagingResult.getTotalElements())
                .build();

        return new ResponseSuccess<>("Thành công",result);
    }
    @Override
    public boolean isRoomOwner(User user, int roomId) {
        boolean isOwner = roomRepository.existsByUserAndId(user,roomId);
        return isOwner;
    }

    @Override
    public ResponseSuccess<RoomVm> joinRoom(String roomPin) {
        var room = roomRepository.findByRoomPin(roomPin);
        if(room.isEmpty()){
            throw new RoomNotFoundException(RoomConstants.ROOM_NOT_FOUND);
        }

        if(room.get().getTimeEnd() != null && room.get().getTimeEnd().before(new Date())){
            throw new RoomClosedException(RoomConstants.ROOM_CLOSED);
        }

        if (room.get().isClosed()){
            throw new RoomClosedException(RoomConstants.ROOM_CLOSED);
        }

        RoomVm roomVm = Utilities.getRoomVm(room.get());

        return new ResponseSuccess<>(RoomConstants.JOIN_ROOM,roomVm);
    }

    @Override
    public ResponseSuccess<Boolean> endRoom(User user,int roomId) {
        var room = roomRepository.findById(roomId);
        if(room.isEmpty()){
            throw new RoomNotFoundException(RoomConstants.ROOM_NOT_FOUND);
        }

        var isOwner = this.isRoomOwner(user,roomId);
        if(!isOwner){
            throw new RoomOwnerException(RoomConstants.NOT_ROOM_OWNER);
        }
        room.get().setClosed(true);
        roomRepository.save(room.get());

        return new ResponseSuccess<>(RoomConstants.END_ROOM,true);

    }

    @Override
    public ResponseSuccess<Boolean> deleteRoom(User user, int roomId) {
        var room = roomRepository.findById(roomId);
        if(room.isEmpty()){
            throw new RoomNotFoundException(RoomConstants.ROOM_NOT_FOUND);
        }

        var isOwner = this.isRoomOwner(user,roomId);
        if(!isOwner){
            throw new RoomOwnerException(RoomConstants.NOT_ROOM_OWNER);
        }

        roomRepository.delete(room.get());
        return new ResponseSuccess<>(RoomConstants.DELETE_ROOM,true);
    }

    @Override
    public ResponseSuccess<Boolean> editRoom(User user, EditRoomDTO dto) {
        var room = roomRepository.findById(dto.getRoomId());
        if(room.isEmpty()){
            throw new RoomNotFoundException(RoomConstants.ROOM_NOT_FOUND);
        }

        var isOwner = this.isRoomOwner(user,dto.getRoomId());
        if(!isOwner){
            throw new RoomOwnerException(RoomConstants.NOT_ROOM_OWNER);
        }

        room.get()
                .setTimeStart(dto.getTimeStart() != null ? dto.getTimeStart() : null);
        room.get()
                .setTimeEnd(dto.getTimeEnd() != null ? dto.getTimeEnd() : null);

        roomRepository.save(room.get());

        return new ResponseSuccess<>(RoomConstants.EDIT_ROOM,true);
    }
}
