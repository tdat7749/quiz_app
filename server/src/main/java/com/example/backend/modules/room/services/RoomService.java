package com.example.backend.modules.room.services;

import com.example.backend.commons.ResponsePaging;
import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.room.dtos.CreateRoomDTO;
import com.example.backend.modules.room.viewmodels.RoomVm;
import com.example.backend.modules.user.models.User;

import java.util.List;

public interface RoomService {
    ResponseSuccess<RoomVm> createRoom(User user, CreateRoomDTO dto);
    ResponseSuccess<Boolean> endRoom(User user);

    ResponseSuccess<RoomVm> editRoom();
    ResponseSuccess<Boolean> deleteRoom(User user,int roomId);

    ResponseSuccess<ResponsePaging<List<RoomVm>>> getMyListRooms(String keyword,String sortBy,int pageIndex); // phân trang

}
