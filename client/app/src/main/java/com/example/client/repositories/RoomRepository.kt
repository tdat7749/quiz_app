package com.example.client.repositories

import com.example.client.model.CreateRoom
import com.example.client.model.EditRoom
import com.example.client.network.room.RoomService
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val roomService:RoomService
) {
    suspend fun createRoom(data:CreateRoom) = ApiHelper.safeCallApi {
        roomService.createRoom(data)
    }

    suspend fun joinRoom(roomPin:String) = ApiHelper.safeCallApi {
        roomService.joinRoom(roomPin)
    }

    suspend fun endRoom(roomId:Int) = ApiHelper.safeCallApi {
        roomService.endRoom(roomId)
    }

    suspend fun editRoom(data:EditRoom) = ApiHelper.safeCallApi {
        roomService.editRoom(data)
    }

    suspend fun deleteRoom(roomId:Int) = ApiHelper.safeCallApi {
        roomService.deleteRoom(roomId)
    }

    suspend fun getMyListRoom(keyword:String,pageIndex:Int,sortBy:String) = ApiHelper.safeCallApi {
        roomService.getMyListRoom(keyword,pageIndex,sortBy)
    }
}