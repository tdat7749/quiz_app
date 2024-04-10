package com.example.client.network.quiz

import com.example.client.model.CreateQuestion
import com.example.client.model.EditQuestion
import com.example.client.model.QuestionDetail
import com.example.client.utils.ApiResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface QuestionService {
    @GET("api/questions/{quizId}")
    suspend fun getListQuestionByQuizId(
        @Path("quizId") quizId:Int
    ): ApiResponse<List<QuestionDetail>>

    @Multipart
    @POST("api/questions/{quizId}")
    suspend fun createQuestion(
        @Part part: List<MultipartBody.Part>,
        @Path("quizId") quizId:Int
    ): ApiResponse<QuestionDetail>

    @PATCH("api/questions/")
    suspend fun editQuestion(
        @Body data:EditQuestion
    ): ApiResponse<QuestionDetail>


    @Multipart
    @PATCH("api/questions/thumbnail")
    suspend fun editQuestionThumbnail(
        @Part thumbnail: List<MultipartBody.Part>
    ): ApiResponse<String>

    @DELETE("api/questions/{questionId}/quiz/{quizId}")
    suspend fun deleteQuestion(
        @Path("questionId") questionId:Int,
        @Path("quizId") quizId:Int
    ): ApiResponse<Boolean>
}