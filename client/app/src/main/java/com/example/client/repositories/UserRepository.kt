package com.example.client.repositories

import com.example.client.model.ChangeDisplayName
import com.example.client.model.ChangePassword
import com.example.client.model.ForgotPassword
import com.example.client.model.SendEmailForgot
import com.example.client.network.user.UserService
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService
) {
    
    suspend fun changePassword(data: ChangePassword) = ApiHelper.safeCallApi {
        userService.changePassword(data)
    }

    suspend fun sendEmailForgotPassword(data:SendEmailForgot) = ApiHelper.safeCallApi {
        userService.sendEmailForgotPassword(data)
    }

    suspend fun changeDisplayName(data: ChangeDisplayName) = ApiHelper.safeCallApi {
        userService.changeDisplayName(data)
    }

    suspend fun getMe() = ApiHelper.safeCallApi {
        userService.getMe()
    }

    suspend fun forgotPassword(data: ForgotPassword) = ApiHelper.safeCallApi {
        userService.forgotPassword(data)
    }
   
}