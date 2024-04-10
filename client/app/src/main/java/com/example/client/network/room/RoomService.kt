package com.example.client.network.room

import com.example.client.model.CreateRoom
import com.example.client.model.EditRoom
import com.example.client.model.Room
import com.example.client.utils.ApiResponse
import com.example.client.utils.PagingResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface RoomService {
    @POST("api/rooms/")
    suspend fun createRoom(
        @Body createRoom:CreateRoom
    ): ApiResponse<Room>

    @GET("api/rooms/{roomPin}/join")
    suspend fun joinRoom(
        @Path("roomPin") roomPin:String
    ): ApiResponse<Int>

    @GET("api/rooms/{roomPin}/participant")
    suspend fun getRoomForParticipants(
        @Path("roomPin") roomPin:String
    ): ApiResponse<Room>

    @GET("api/rooms/{roomId}")
    suspend fun getRoomDetail(
        @Path("roomId") roomId:Int
    ): ApiResponse<Room>

    @PATCH("api/rooms/{roomId}/end")
    suspend fun endRoom(
        @Path("roomId") roomId:Int
    ): ApiResponse<Boolean>

    @PATCH("api/rooms/")
    suspend fun editRoom(
        @Body editRoom:EditRoom
    ): ApiResponse<Boolean>

    @DELETE("api/rooms/{roomId}")
    suspend fun deleteRoom(
        @Path("roomId") roomId:Int
    ): ApiResponse<Boolean>

    @GET("api/rooms/")
    suspend fun getMyListRoom(
        @Query("keyword") keyword:String,
        @Query("pageIndex") pageIndex:Int,
        @Query("sortBy") sortBy:String = "createdAt",
    ): ApiResponse<PagingResponse<List<Room>>>

    @GET("api/rooms/joined")
    suspend fun getJoinedRoom(
        @Query("pageIndex") pageIndex:Int,
    ): ApiResponse<PagingResponse<List<Room>>>

    @DELETE("api/rooms/{roomId}/kick/{userId}")
    suspend fun kickUser(
        @Path("roomId") roomId:Int,
        @Path("userId") userId:Int
    ): ApiResponse<Boolean>
}