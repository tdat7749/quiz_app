package com.example.client.model

data class AuthToken(
    val accessToken:String,
    val refreshToken:String
)

data class Login(
    val userName:String,
    val password:String
)

data class Register(
    val userName:String,
    val password:String,
    val confirmPassword:String,
    val email:String,
    val displayName:String
)

data class Verify(
    val code:String,
    val email:String
)

data class User(
    val id:Int,
    val displayName:String,
    val avatar :String
)