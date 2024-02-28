package com.example.client.repositories

import com.example.client.model.Login
import com.example.client.network.auth.AuthService
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService
): BaseRepository(){
    suspend fun login(login:Login) = safeCallApi {
        authService.login(login)
    }
}