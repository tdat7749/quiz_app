package com.example.client.network.history

import com.example.client.model.CreateHistory
import com.example.client.utils.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface HistoryService {
    @POST("api/histories/")
    suspend fun createHistory(
        @Body createHistory: CreateHistory
    ): ApiResponse<Boolean>
}