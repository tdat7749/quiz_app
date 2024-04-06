package com.example.client.network.user

import com.example.client.model.*
import com.example.client.utils.ApiResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface UserService {
    @GET("api/users/me")
    suspend fun getMe() : ApiResponse<User>

    @GET("api/users/me/detail")
    suspend fun getMeDetail() : ApiResponse<UserDetail>

    @PATCH("api/users/forgot")
    suspend fun forgotPassword(@Body forgotPassword: ForgotPassword): ApiResponse<Boolean>

    @PATCH("api/users/password")
    suspend fun changePassword(@Body changePassword: ChangePassword) : ApiResponse<Boolean>

    @PATCH("api/users/name")
    suspend fun changeDisplayName(@Body changeDisplayName: ChangeDisplayName) : ApiResponse<String>

    @POST("api/users/forgot-mail")
    suspend fun sendEmailForgotPassword(
        @Body data: SendEmailForgot
    ) :  ApiResponse<Boolean>

    @Multipart
    @PATCH("api/users/avatar")
    suspend fun changeAvatar(@Part avatar: List<MultipartBody.Part>):ApiResponse<String>
}