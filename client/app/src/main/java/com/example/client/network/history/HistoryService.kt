package com.example.client.network.history

import com.example.client.model.CreateHistory
import com.example.client.model.HistoryAnswer
import com.example.client.model.HistoryRank
import com.example.client.utils.ApiResponse
import com.example.client.utils.PagingResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HistoryService {
    @POST("api/histories/")
    suspend fun createHistory(
        @Body createHistory: CreateHistory
    ): ApiResponse<Boolean>

    @GET("api/histories/room/{roomId}/rank")
    suspend fun getHistoryRankRoom(
        @Path("roomId") roomId:Int,
        @Query("pageIndex") page:Int
    ): ApiResponse<PagingResponse<List<HistoryRank>>>

    @GET("api/histories/single/{quizId}/rank")
    suspend fun getHistoryRankSingle(
        @Path("quizId") quizId:Int,
        @Query("pageIndex") page:Int
    ): ApiResponse<PagingResponse<List<HistoryRank>>>

    @GET("api/histories/{roomId}/answer")
    suspend fun getHistoryAnswer(
        @Path("roomId") roomId:Int
    ): ApiResponse<List<HistoryAnswer>>
}