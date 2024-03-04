package com.example.client.network.auth

import com.example.client.utils.ApiResponse
import com.example.client.model.AuthToken
import com.example.client.model.Login
import com.example.client.model.Register
import com.example.client.model.Verify
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {
    @POST("api/auth/login")
    suspend fun login(@Body login: Login) : ApiResponse<AuthToken>

    @POST("api/auth/register")
    suspend fun register(@Body register:Register): ApiResponse<Boolean>

    @POST("api/auth/verify")
    suspend fun verify(@Body verify: Verify): ApiResponse<Boolean>
}