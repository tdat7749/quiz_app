package com.example.client.network.auth

import com.example.client.model.AuthToken
import retrofit2.Response
import javax.inject.Inject

class AuthServiceImpl : AuthService {
    override suspend fun login(userName: String, password: String): Response<AuthToken> {
        TODO("Not yet implemented")
    }
}