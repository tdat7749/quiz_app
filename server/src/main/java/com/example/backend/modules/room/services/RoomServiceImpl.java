package com.example.backend.modules.room.services;

import com.example.backend.commons.AppConstants;
import com.example.backend.commons.ResponsePaging;
import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.quiz.constant.QuizConstants;
import com.example.backend.modules.quiz.exceptions.NotOwnerQuizException;
import com.example.backend.modules.quiz.exceptions.QuizHasNotQuestions;
import com.example.backend.modules.quiz.exceptions.QuizNotFoundException;
import com.example.backend.modules.quiz.services.QuizService;
import com.example.backend.modules.quiz.viewmodels.QuestionDetailVm;
import com.example.backend.modules.quiz.viewmodels.QuestionVm;
import com.example.backend.modules.room.constant.RoomConstants;
import com.example.backend.modules.room.dtos.CreateRoomDTO;
import com.example.backend.modules.room.dtos.EditRoomDTO;
import com.example.backend.modules.room.exceptions.*;
import com.example.backend.modules.room.models.Room;
import com.example.backend.modules.room.repositories.RoomRepository;
import com.example.backend.modules.room.viewmodels.JoinRoomVm;
import com.example.backend.modules.room.viewmodels.RoomRealTime;
import com.example.backend.modules.room.viewmodels.RoomVm;
import com.example.backend.modules.user.constant.UserConstants;
import com.example.backend.modules.user.exceptions.UserNotFoundException;
import com.example.backend.modules.user.models.User;
import com.example.backend.modules.user.repositories.UserRepository;
import com.example.backend.modules.user.services.UserService;
import com.example.backend.utils.Utilities;
import com.google.firebase.database.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class RoomServiceImpl implements RoomService{
    private final QuizService quizService;
    private final RoomRepository roomRepository;
    private final UserService userService;
    private final GameModeService gameModeService;
    private final UserRepository userRepository;

    public RoomServiceImpl(
            QuizService quizService,
            RoomRepository roomRepository,
            UserService userService,
            GameModeService gameModeService,
            UserRepository userRepository
    ){
        this.quizService = quizService;
        this.roomRepository = roomRepository;
        this.userService = userService;
        this.gameModeService = gameModeService;
        this.userRepository = userRepository;
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
        var listQuestion = quiz.get().getQuestions();

        if(listQuestion.isEmpty()){
            throw new QuizHasNotQuestions(QuizConstants.QUIZ_HAS_NOT_QUESTION);
        }

        var gameMode = gameModeService.findById(dto.getModeId());
        if(gameMode.isEmpty()){
            throw new GameModeNotFoundException(RoomConstants.GAME_MODE_NOT_FOUND);
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
                .gameMode(gameMode.get())
                .build();

        var save = roomRepository.save(newRoom);
        RoomVm roomVm = Utilities.getRoomVm(save);

        if(gameMode.get().getModeCode().equals("realtime")){
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference ref = database.getReference("rooms").child(roomVm.getRoomPin() + roomVm.getId());
            List<QuestionDetailVm> questions = listQuestion.stream().map(Utilities::getQuestionDetailVm).toList();
            RoomRealTime rrt = Utilities.getRoomRealTimeVm(save,questions);
            rrt.setTotalUser(this.countUserInRoom(save));
            rrt.setUsers(new ArrayList<>());

            ref.setValueAsync(rrt);
        }

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
    @Transactional
    public ResponseSuccess<Boolean> leaveRoom(User user, int roomId) {
        var foundedRoom = roomRepository.findById(roomId);
        if(foundedRoom.isEmpty()){
            throw new RoomNotFoundException(RoomConstants.ROOM_NOT_FOUND);
        }
        var foundedUser = userService.findById(user.getId());
        if(foundedUser.isEmpty()){
            throw new UserNotFoundException(UserConstants.USER_NOT_FOUND);
        }

        foundedRoom.get().getUsers().remove(foundedUser.get());
        foundedUser.get().getUserRooms().remove(foundedRoom.get());

        roomRepository.save(foundedRoom.get());
        userRepository.save(foundedUser.get());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference roomRef = database.getReference("rooms").child(foundedRoom.get().getRoomPin() + foundedRoom.get().getId()).child("users");

        roomRef.orderByChild("id").equalTo(String.valueOf(user.getId())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    userSnapshot.getRef().removeValueAsync();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        DatabaseReference ref = database.getReference("rooms").child(foundedRoom.get().getRoomPin() + foundedRoom.get().getId());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Lấy giá trị từ dataSnapshot
                    Long currentTotaluser = dataSnapshot.child("totalUser").getValue(Long.class);

                    // Cập nhật giá trị mới vào node con
                    ref.child("totalUser").setValueAsync(currentTotaluser - 1);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return new ResponseSuccess<>("Rời phòng thành công",true);
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
    public ResponseSuccess<JoinRoomVm> joinRoom(User user,String roomPin) {
        var room = roomRepository.findByRoomPin(roomPin);
        if(room.isEmpty()){
            throw new RoomNotFoundException(RoomConstants.ROOM_NOT_FOUND);
        }
        Room roomm = room.get();
        if(roomm.getTimeStart() != null && roomm.getTimeStart().isAfter(LocalDateTime.of(LocalDate.now(), LocalTime.now()))){
            throw new RoomHasNotStarted(RoomConstants.ROOM_HAS_NOT_STARTED);
        }

        if(roomm.getTimeEnd() != null && roomm.getTimeEnd().isBefore(LocalDateTime.of(LocalDate.now(), LocalTime.now()))){
            throw new RoomClosedException(RoomConstants.ROOM_CLOSED);
        }

        if (roomm.isClosed()){
            throw new RoomClosedException(RoomConstants.ROOM_CLOSED);
        }

        long totalUserInRoom = this.countUserInRoom(roomm);

        if(totalUserInRoom + 1 > roomm.getMaxUser()){
            throw new RoomFullException(RoomConstants.ROOM_FULL);
        }

        JoinRoomVm vm = Utilities.getJoinRoomVm(room.get().getId(),room.get().getGameMode());

        // kiểm tra user đã tham gia phòng chưa, tham gia rồi thì return về phòng luôn
        if(roomRepository.existsByIdAndUser(room.get().getId(),user.getId())){
            return new ResponseSuccess<>(RoomConstants.JOIN_ROOM,vm);
        }

        room.get().getUsers().add(user);
        roomRepository.save(room.get());

        if (roomm.getGameMode().getModeCode().equals("realtime")){
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference ref = database.getReference("rooms").child(roomm.getRoomPin() + roomm.getId());

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Lấy giá trị từ dataSnapshot
                        Long currentTotaluser = dataSnapshot.child("totalUser").getValue(Long.class);


                        // Cập nhật giá trị mới vào node con
                        ref.child("totalUser").setValueAsync(currentTotaluser + 1);


                        DatabaseReference usersRef = ref.child("users");

                        usersRef.child(String.valueOf(user.getId())).setValueAsync(Utilities.getUserVm(user));
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        return new ResponseSuccess<>(RoomConstants.JOIN_ROOM,vm);
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
