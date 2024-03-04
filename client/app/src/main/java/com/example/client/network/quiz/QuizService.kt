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
        @Path("topicId") topicId:Int,
        @Query("keyword") keyword:String,
        @Query("pageIndex") pageIndex:Int,
        @Query("sortBy") sortBy:String = "createdAt"
    ) : ApiResponse<PagingResponse<List<Quiz>>>

    @GET("api/quizzes/my")
    suspend fun getMyQuizzes(
        @Query("keyword") keyword:String,
        @Query("pageIndex") pageIndex:Int,
        @Query("sortBy") sortBy:String = "createdAt",
    ) : ApiResponse<PagingResponse<List<Quiz>>>

    @GET("api/quizzes/{quizId}")
    suspend fun getMyQuizzes(
        @Path("quizId") topicId:Int
    )

    @GET("api/quizzes/latest")
    suspend fun get10QuizLatest(): ApiResponse<List<Quiz>>

    @GET("api/quizzes/top-10")
    suspend fun getTop10QuizCollection(): ApiResponse<List<Quiz>>
}