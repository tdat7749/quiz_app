package com.example.client.repositories

import com.example.client.model.Login
import com.example.client.network.auth.AuthService
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService
){
    suspend fun login(login:Login) = ApiHelper.safeCallApi {
        authService.login(login)
    }
}