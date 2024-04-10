package com.example.backend.modules.room.services;

import com.example.backend.commons.AppConstants;
import com.example.backend.commons.ResponsePaging;
import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.quiz.constant.QuizConstants;
import com.example.backend.modules.quiz.exceptions.NotOwnerQuizException;
import com.example.backend.modules.quiz.exceptions.QuizHasNotQuestions;
import com.example.backend.modules.quiz.exceptions.QuizNotFoundException;
import com.example.backend.modules.quiz.services.QuizService;
import com.example.backend.modules.room.constant.RoomConstants;
import com.example.backend.modules.room.dtos.CreateRoomDTO;
import com.example.backend.modules.room.dtos.EditRoomDTO;
import com.example.backend.modules.room.exceptions.*;
import com.example.backend.modules.room.models.Room;
import com.example.backend.modules.room.repositories.RoomRepository;
import com.example.backend.modules.room.viewmodels.RoomVm;
import com.example.backend.modules.user.constant.UserConstants;
import com.example.backend.modules.user.exceptions.UserNotFoundException;
import com.example.backend.modules.user.models.User;
import com.example.backend.modules.user.services.UserService;
import com.example.backend.utils.Utilities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class RoomServiceImpl implements RoomService{
    private final QuizService quizService;
    private final RoomRepository roomRepository;
    private final UserService userService;

    public RoomServiceImpl(
            QuizService quizService,
            RoomRepository roomRepository,
            UserService userService
    ){
        this.quizService = quizService;
        this.roomRepository = roomRepository;
        this.userService = userService;
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

        if(quiz.get().getQuestions().isEmpty()){
            throw new QuizHasNotQuestions(QuizConstants.QUIZ_HAS_NOT_QUESTION);
        }

        var newRoom = Room.builder()
                .createdAt(new Date())
                .timeStart(dto.getTimeStart() != null ? dto.getTimeStart() : null)
                .timeEnd(dto.getTimeEnd() != null ? dto.getTimeEnd() : null)
                .quiz(quiz.get())
                .user(user)
                .roomPin(Utilities.generateCode())
                .isClosed(false)
                .roomName(dto.getRoomName())
                .playAgain(dto.isPlayAgain())
                .maxUser(dto.getMaxUser())
                .build();

        var save = roomRepository.save(newRoom);
        RoomVm roomVm = Utilities.getRoomVm(save);

        return new ResponseSuccess<>(RoomConstants.CREATE_ROOM_SUCCESS,roomVm);
    }

    @Override
    public ResponseSuccess<ResponsePaging<List<RoomVm>>> getMyListRooms(String keyword, String sortBy, int pageIndex,User user) {
        Pageable paging = PageRequest.of(pageIndex, AppConstants.PAGE_SIZE, Sort.by(Sort.Direction.DESC,sortBy));

        Page<Room> pagingResult = roomRepository.getMyListRooms(user,keyword,paging);
        List<RoomVm> roomVmList = pagingResult.stream().map(Utilities::getRoomVm).toList();

        ResponsePaging result = ResponsePaging.builder()
                .data(roomVmList)
                .totalPage(pagingResult.getTotalPages())
                .totalRecord((int) pagingResult.getTotalElements())
                .build();

        return new ResponseSuccess<>("Thành công",result);
    }

    @Override
    public ResponseSuccess<RoomVm> getRoomDetail(int roomId) {
        var room = roomRepository.findById(roomId);
        if(room.isEmpty()){
            throw new RoomNotFoundException(RoomConstants.ROOM_NOT_FOUND);
        }

        long totalUserInRoom = this.countUserInRoom(room.get());
        RoomVm roomVm = Utilities.getRoomVm(room.get(),totalUserInRoom);

        return new ResponseSuccess<>("Thành Công",roomVm);
    }

    @Override
    public ResponseSuccess<ResponsePaging<List<RoomVm>>> getJoinedRoom(User user, int pageIndex) {
        Pageable paging = PageRequest.of(pageIndex, AppConstants.PAGE_SIZE, Sort.by(Sort.Direction.DESC,"createdAt"));

        Page<Room> pagingResult = roomRepository.getJoinedRoom(user,paging);
        List<RoomVm> roomVmList = pagingResult.stream().map(Utilities::getRoomVm).toList();

        ResponsePaging result = ResponsePaging.builder()
                .data(roomVmList)
                .totalPage(pagingResult.getTotalPages())
                .totalRecord((int) pagingResult.getTotalElements())
                .build();

        return new ResponseSuccess<>("Thành công",result);
    }

    @Override
    public ResponseSuccess<Boolean> kickUser(User user, int roomId, int userId) {
        var foundedRoom = roomRepository.findById(roomId);
        if(foundedRoom.isEmpty()){
            throw new RoomNotFoundException(RoomConstants.ROOM_NOT_FOUND);
        }

        var foundedUser = userService.findById(userId);
        if(foundedUser.isEmpty()){
            throw new UserNotFoundException(UserConstants.USER_NOT_FOUND);
        }

        var isOwner = this.isRoomOwner(user,foundedRoom.get().getId());
        if(!isOwner){
            throw new RoomOwnerException(RoomConstants.NOT_ROOM_OWNER);
        }

        foundedRoom.get().getUsers().remove(foundedUser.get());
        foundedUser.get().getUserRooms().remove(foundedRoom.get());

        roomRepository.save(foundedRoom.get());

        return new ResponseSuccess<>("Đuổi người chơi thành công",true);
    }

    @Override
    public boolean isRoomOwner(User user, int roomId) {
        boolean isOwner = roomRepository.existsByUserAndId(user,roomId);
        return isOwner;
    }

    @Override
    public long countUserInRoom(Room room) {
        return roomRepository.countUserInRoom(room);
    }

    @Override
    @Transactional
    public ResponseSuccess<Integer> joinRoom(User user,String roomPin) {
        var room = roomRepository.findByRoomPin(roomPin);
        if(room.isEmpty()){
            throw new RoomNotFoundException(RoomConstants.ROOM_NOT_FOUND);
        }

        if(room.get().getTimeStart() != null && room.get().getTimeStart().isAfter(LocalDateTime.of(LocalDate.now(), LocalTime.now()))){
            throw new RoomHasNotStarted(RoomConstants.ROOM_HAS_NOT_STARTED);
        }

        if(room.get().getTimeEnd() != null && room.get().getTimeEnd().isBefore(LocalDateTime.of(LocalDate.now(), LocalTime.now()))){
            throw new RoomClosedException(RoomConstants.ROOM_CLOSED);
        }

        if (room.get().isClosed()){
            throw new RoomClosedException(RoomConstants.ROOM_CLOSED);
        }

//        var isOnwer = this.isRoomOwner(user,room.get().getId());
        // Chủ phòng join thì không cần add
//        if(isOnwer){
//            return new ResponseSuccess<>(RoomConstants.JOIN_ROOM,room.get().getId());
//        }

        long totalUserInRoom = this.countUserInRoom(room.get());

        if(totalUserInRoom + 1 > room.get().getMaxUser()){
            throw new RoomFullException(RoomConstants.ROOM_FULL);
        }


        // kiểm tra user đã tham gia phòng chưa, tham gia rồi thì return về phòng luôn
        if(roomRepository.existsByIdAndUser(room.get().getId(),user.getId())){
            return new ResponseSuccess<>(RoomConstants.JOIN_ROOM,room.get().getId());
        }

        room.get().getUsers().add(user);
        roomRepository.save(room.get());

        return new ResponseSuccess<>(RoomConstants.JOIN_ROOM,room.get().getId());
    }

    @Override
    public ResponseSuccess<RoomVm> getRoomForParticipants(User user, String roomPin) {
        var room = roomRepository.findByRoomPin(roomPin);
        if(room.isEmpty()){
            throw new RoomNotFoundException(RoomConstants.ROOM_NOT_FOUND);
        }
        boolean isOnwer = this.isRoomOwner(user,room.get().getId());
        if(!isOnwer){
            var isJoin = roomRepository.existsByIdAndUser(room.get().getId(), user.getId());
            if(!isJoin){
                throw new UserIsNotInRoomException(RoomConstants.USER_IS_NOT_IN_ROOM);
            }
        }
        long totalUserInRoom = roomRepository.countUserInRoom(room.get());
        RoomVm roomVm = Utilities.getRoomVm(room.get(),totalUserInRoom);
        roomVm.setOnwer(isOnwer);

        return new ResponseSuccess<>("Thành Công",roomVm);
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

        room.get()
                .setRoomName(dto.getRoomName());
        room.get()
                        .setClosed(dto.isClosed());
        room.get()
                        .setMaxUser(dto.getMaxUser());
        room.get()
                        .setPlayAgain(dto.isPlayAgain());

        roomRepository.save(room.get());

        return new ResponseSuccess<>(RoomConstants.EDIT_ROOM,true);
    }
}
