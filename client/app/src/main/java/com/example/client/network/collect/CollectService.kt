package com.example.client.network.collect

import com.example.client.utils.ApiResponse
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface CollectService {
    @POST("api/collections/{quizId}")
    suspend fun addToCollection(
        @Path("quizId") quizId:Int
    ): ApiResponse<Boolean>

    @DELETE("api/collections/{quizId}")
    suspend fun removeFromCollection(
        @Path("quizId") quizId:Int
    ): ApiResponse<Boolean>
}