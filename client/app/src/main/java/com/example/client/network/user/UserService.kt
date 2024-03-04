package com.example.client.network.user

import com.example.client.model.ChangeDisplayName
import com.example.client.model.ChangePassword
import com.example.client.model.ForgotPassword
import com.example.client.model.User
import com.example.client.utils.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface UserService {
    @GET("api/users/me")
    suspend fun getMe() : ApiResponse<User>

    @PATCH("api/users/forgot")
    suspend fun forgotPassword(@Body forgotPassword: ForgotPassword): ApiResponse<Boolean>

    @PATCH("api/users/password")
    suspend fun changePassword(@Body changePassword: ChangePassword) : ApiResponse<Boolean>

    @PATCH("api/users/name")
    suspend fun changeDisplayName(@Body changeDisplayName: ChangeDisplayName) : ApiResponse<Boolean>

}