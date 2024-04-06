package com.example.client.repositories

import com.example.client.model.*
import com.example.client.network.user.UserService
import com.example.client.utils.Utilities
import okhttp3.MultipartBody
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

    suspend fun getMeDetail() = ApiHelper.safeCallApi {
        userService.getMeDetail()
    }

    suspend fun forgotPassword(data: ForgotPassword) = ApiHelper.safeCallApi {
        userService.forgotPassword(data)
    }

    suspend fun changeAvatar(data: ChangeAvatar) = ApiHelper.safeCallApi{
        val avatar = mutableListOf<MultipartBody.Part>()
        avatar.add(Utilities.Companion.createFilePart("avatar", data.avatar))

        userService.changeAvatar(avatar)
    }
   
}