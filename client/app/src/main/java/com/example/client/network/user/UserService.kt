package com.example.client.network.user

import com.example.client.model.*
import com.example.client.utils.ApiResponse
import retrofit2.http.*

interface UserService {
    @GET("api/users/me")
    suspend fun getMe() : ApiResponse<User>

    @PATCH("api/users/forgot")
    suspend fun forgotPassword(@Body forgotPassword: ForgotPassword): ApiResponse<Boolean>

    @PATCH("api/users/password")
    suspend fun changePassword(@Body changePassword: ChangePassword) : ApiResponse<Boolean>

    @PATCH("api/users/name")
    suspend fun changeDisplayName(@Body changeDisplayName: ChangeDisplayName) : ApiResponse<Boolean>

    @POST("api/users/forgot-mail")
    suspend fun sendEmailForgotPassword(
        @Body data: SendEmailForgot
    ) :  ApiResponse<Boolean>

}