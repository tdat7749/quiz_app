package com.example.client.model

data class Quiz(
    val id:Int,
    val title:String,
    val slug:String,
    val thumbnail:String,
    val user: User
)