package com.example.client.model

data class AuthToken(
    val accessToken:String,
    val refreshToken:String
)

data class Login(
    val userName:String,
    val password:String
)

data class LoginWithGoogle(
    val displayName: String,
    val email:String,
    val uid:String,
    var avatar:String
)

data class Register(
    val userName:String,
    val password:String,
    val confirmPassword:String,
    val email:String,
    val displayName:String
)

data class Verify(
    val token:String,
    val email:String
)

data class User(
    val id:Int,
    val displayName:String,
    val avatar :String
){
    constructor(): this(
        -1,
        "",
        ""
    )
}

data class UserDetail(
    val id:Int,
    val displayName:String,
    val avatar :String,
    val role:String,
    val userName:String,
    val email:String
)

data class ResendEmail(
    val email:String
)