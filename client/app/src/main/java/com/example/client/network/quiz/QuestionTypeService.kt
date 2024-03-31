package com.example.client.network.quiz

import com.example.client.model.QuestionType
import com.example.client.utils.ApiResponse
import retrofit2.http.GET

interface QuestionTypeService {
    @GET("/api/types/")
    suspend fun getAllQuestionType() : ApiResponse<List<QuestionType>>
}