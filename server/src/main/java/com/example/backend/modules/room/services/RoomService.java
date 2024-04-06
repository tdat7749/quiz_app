package com.example.backend.modules.room.services;

import com.example.backend.commons.ResponsePaging;
import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.room.dtos.CreateRoomDTO;
import com.example.backend.modules.room.dtos.EditRoomDTO;
import com.example.backend.modules.room.models.Room;
import com.example.backend.modules.room.viewmodels.RoomVm;
import com.example.backend.modules.user.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface RoomService {

    Optional<Room> findById(int id);

    boolean isRoomOwner(User user, int roomId);

    ResponseSuccess<RoomVm> joinRoom(String roomPin);

    ResponseSuccess<RoomVm> createRoom(User user, CreateRoomDTO dto);
    ResponseSuccess<Boolean> endRoom(User user,int roomId);

    ResponseSuccess<Boolean> editRoom(User user, EditRoomDTO dto);
    ResponseSuccess<Boolean> deleteRoom(User user,int roomId);

    ResponseSuccess<ResponsePaging<List<RoomVm>>> getMyListRooms(String keyword,String sortBy,int pageIndex,User user); // phân trang

    ResponseSuccess<RoomVm> getRoomDetail(int roomId);

}
