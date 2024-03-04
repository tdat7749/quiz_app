package com.example.client.repositories

import com.example.client.model.Login
import com.example.client.model.Register
import com.example.client.model.Verify
import com.example.client.network.auth.AuthService
import com.example.client.network.user.UserService
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService
){
    suspend fun login(login:Login) = ApiHelper.safeCallApi {
        authService.login(login)
    }

    suspend fun register(register: Register) = ApiHelper.safeCallApi {
        authService.register(register)
    }

    suspend fun verify(verify: Verify) = ApiHelper.safeCallApi {
        authService.verify(verify)
    }
}