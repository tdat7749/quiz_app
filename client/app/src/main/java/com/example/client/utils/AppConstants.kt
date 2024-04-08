package com.example.client.utils

object AppConstants {
    const val APP_BASE_URL = "http://192.168.1.4:8080/"
    const val ACCESS_TOKEN = "access_token"
    const val REFRESH_TOKEN = "refresh_token"

    val PUBLIC_API: ArrayList<String> = arrayListOf(
        "/api/auth/login",
        "/api/auth/register",
        "/api/quizzes/public",
        "/api/topics/",
        "/api/auth/verify",
        "/api/auth/resend",
        "/api/auth/refresh",
        "/api/quizzes/top-10",
        "/api/quizzes/latest",
        "/api/users/forgot-mail",
        "/api/users/forgot",
        "/api/types/"
    )
}