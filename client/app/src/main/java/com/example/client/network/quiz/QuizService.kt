package com.example.client.network.quiz

import androidx.paging.PagingSource
import com.example.client.model.Quiz
import com.example.client.utils.ApiResponse
import com.example.client.utils.PagingResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface QuizService {
    @GET("api/quizzes/public/{topicId}/topic")
    suspend fun getPublicQuizzes(
        @Query("keyword") keyword:String,
        @Query("pageIndex") pageIndex:Int,
        @Query("sortBy") sortBy:String = "createdAt",
        @Path("topicId") topicId:Int
    ) : ApiResponse<PagingResponse<List<Quiz>>>

    @GET("api/quizzes/my")
    suspend fun getMyQuizzes(
        @Query("keyword") keyword:String,
        @Query("pageIndex") pageIndex:Int,
        @Query("sortBy") sortBy:String = "createdAt",
    )

    @GET("api/quizzes/{quizId}")
    suspend fun getMyQuizzes(
        @Path("quizId") topicId:Int
    )
}