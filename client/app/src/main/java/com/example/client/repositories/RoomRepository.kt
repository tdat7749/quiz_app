package com.example.client.repositories

import com.example.client.model.CreateRoom
import com.example.client.model.EditRoom
import com.example.client.network.room.RoomService
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val roomService:RoomService
) {
    
    suspend fun getMyListRoom(keyword:String,pageIndex:Int,sortBy:String) = ApiHelper.safeCallApi {
        roomService.getMyListRoom(keyword,pageIndex,sortBy)
    }

    suspend fun getRoomDetail(roomId:Int) = ApiHelper.safeCallApi {
        roomService.getRoomDetail(roomId)
    }

    suspend fun editRoom(data:EditRoom) = ApiHelper.safeCallApi {
        roomService.editRoom(data)
    }

    suspend fun deleteRoom(roomId:Int) = ApiHelper.safeCallApi {
        roomService.deleteRoom(roomId)
    }


    suspend fun createRoom(data:CreateRoom) = ApiHelper.safeCallApi {
        roomService.createRoom(data)
    }

    suspend fun joinRoom(roomPin:String) = ApiHelper.safeCallApi {
        roomService.joinRoom(roomPin)
    }

    suspend fun getRoomForParticipants(roomPin: String) = ApiHelper.safeCallApi {
        roomService.getRoomForParticipants(roomPin)
    }

    suspend fun endRoom(roomId:Int) = ApiHelper.safeCallApi {
        roomService.endRoom(roomId)
    }

    suspend fun getJoinedRoom(pageIndex:Int) = ApiHelper.safeCallApi {
        roomService.getJoinedRoom(pageIndex)
    }

    suspend fun getListGameMode() = ApiHelper.safeCallApi {
        roomService.getListGameMode()
    }

    suspend fun leaveRoom(roomId:Int) = ApiHelper.safeCallApi {
        roomService.leaveRoom(roomId)
    }
}