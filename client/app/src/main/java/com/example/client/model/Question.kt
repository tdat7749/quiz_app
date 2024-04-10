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

data class Question(
    val id:Int,
    val title:String,
    val answers:List<Answer>,
    val thumbnail:String?,
    val score:Int
)

data class EditQuestion(
    val questionId:Int,
    val title:String,
    val score:Int,
    val timeLimit:Int,
    val quizId:Int,
    val questionTypeId:Int
)

data class EditQuestionThumbnail(
    val questionId:Int,
    val quizId:Int,
    val thumbnail:String
)

data class QuestionType(
    val id:Int,
    val title:String
)