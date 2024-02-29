package com.example.client.model

data class AuthToken(
    val accessToken:String,
    val refreshToken:String
)

data class Login(
    val userName:String,
    val password:String
)

data class User(
    val id:Int,
    val displayName:String,
    val avatar :String
)