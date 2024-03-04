package com.example.client.model

data class ForgotPassword(
    val token:String,
    val email:String,
    val newPassword:String,
    val confirmPassword:String
)

data class ChangePassword(
    val newPassword: String,
    val confirmPassword: String,
    val oldPassword: String,
)

data class ChangeDisplayName(
    val displayName: String
)