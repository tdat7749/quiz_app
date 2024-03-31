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

data class QuestionType(
    val id:Int,
    val title:String
)