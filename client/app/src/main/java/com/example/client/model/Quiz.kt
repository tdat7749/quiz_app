package com.example.client.model

import android.net.Uri

data class Quiz(
    val id:Int,
    val title:String,
    val slug:String,
    val thumbnail:String,
    val user: User
)

data class CreateQuiz(
    val summary:String,
    val description:String,
    val thumbnail: String,
    val title:String,
    val topicId: Int,
    val isPublic: String,
    val questions : List<CreateQuestion>
)