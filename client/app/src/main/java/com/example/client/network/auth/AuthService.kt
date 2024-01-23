package com.example.client.network.auth

import com.example.client.model.AuthToken
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/auth/login") // nên đổi thành 1 POJO class
    suspend fun login(@Body userName:String, @Body password:String) : Response<AuthToken>
}