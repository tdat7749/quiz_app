package com.example.client.model

import android.net.Uri

data class CreateQuestion (
    val title:String,
    val questionType:QuestionType,
    val order:Int,
    val score:Int,
    val timeLimit:Int,
    val answers:List<CreateAnswer>,
    val thumbnail: String?,
    val uri: Uri?
)

data class QuestionDetail(
    val id:Int,
    val title:String,
    val score:Int,
    val timeLimit:Int,
    val answers:List<Answer>,
    val thumbnail: String?,
    val order:Int,
    val questionType:QuestionType,
)

data class QuestionType(
    val id:Int,
    val title:String
)