package com.example.client.network.auth

import com.example.client.utils.ApiResponse
import com.example.client.model.AuthToken
import com.example.client.model.Login
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("api/auth/login")
    suspend fun login(@Body login: Login) : ApiResponse<AuthToken>
}