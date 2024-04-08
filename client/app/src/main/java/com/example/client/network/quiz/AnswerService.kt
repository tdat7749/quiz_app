package com.example.client.network.quiz

import com.example.client.model.Answer
import com.example.client.model.EditListAnswer
import com.example.client.utils.ApiResponse
import retrofit2.http.Body
import retrofit2.http.PATCH

interface AnswerService {
    @PATCH("api/answers/")
    suspend fun editAnswers(
        @Body data:EditListAnswer
    ): ApiResponse<List<Answer>>
}