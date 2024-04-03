package com.example.client.model

data class CreateAnswer(
    val title:String,
    val isCorrect:Boolean
)

data class Answer(
    val id:Int,
    val title:String,
    val correct:Boolean
)