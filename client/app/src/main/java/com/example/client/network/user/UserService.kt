package com.example.client.network.user

import com.example.client.model.User
import com.example.client.utils.ApiResponse
import retrofit2.http.GET

interface UserService {
    @GET("api/users/me")
    suspend fun getMe() : ApiResponse<User> ;
}