package com.example.client.network.quiz

import com.example.client.model.Quiz
import com.example.client.model.QuizDetail
import com.example.client.utils.ApiResponse
import com.example.client.utils.PagingResponse
import okhttp3.MultipartBody
import retrofit2.http.*

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


    @GET("api/quizzes/latest")
    suspend fun get10QuizLatest(): ApiResponse<List<Quiz>>

    @GET("api/quizzes/top-10")
    suspend fun getTop10QuizCollection(): ApiResponse<List<Quiz>>


    @Multipart
    @POST("api/quizzes/")
    suspend fun createQuiz(
        @Part parts: List<MultipartBody.Part>
    ):ApiResponse<Quiz>

    @GET("api/quizzes/{quizId}")
    suspend fun getQuizDetail(
        @Path("quizId") quizId:Int
    ): ApiResponse<QuizDetail>
}