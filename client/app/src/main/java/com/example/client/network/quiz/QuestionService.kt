package com.example.client.network.quiz

import com.example.client.model.QuestionDetail
import com.example.client.utils.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface QuestionService {
    @GET("api/questions/{quizId}")
    suspend fun getListQuestionByQuizId(
        @Path("quizId") quizId:Int
    ): ApiResponse<List<QuestionDetail>>
}