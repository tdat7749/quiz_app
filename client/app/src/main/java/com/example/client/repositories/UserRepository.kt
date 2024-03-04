package com.example.client.repositories

import com.example.client.network.user.UserService
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService
) {
    suspend fun getMe() = ApiHelper.safeCallApi {
        userService.getMe()
    }
}