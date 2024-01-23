package com.example.backend.modules.room.services;

import com.example.backend.commons.ResponsePaging;
import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.room.dtos.CreateRoomDTO;
import com.example.backend.modules.room.viewmodels.RoomVm;
import com.example.backend.modules.user.models.User;

import java.util.List;

public class RoomServiceImpl implements RoomService{
    @Override
    public ResponseSuccess<RoomVm> createRoom(User user, CreateRoomDTO dto) {
        return null;
    }

    @Override
    public ResponseSuccess<Boolean> endRoom(User user) {
        return null;
    }

    @Override
    public ResponseSuccess<RoomVm> editRoom() {
        return null;
    }

    @Override
    public ResponseSuccess<Boolean> deleteRoom(User user, int roomId) {
        return null;
    }

    @Override
    public ResponseSuccess<ResponsePaging<List<RoomVm>>> getMyListRooms(String keyword, String sortBy, int pageIndex) {
        return null;
    }
}
