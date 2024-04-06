package com.example.client.model

import android.net.Uri

data class Quiz(
    val id:Int,
    val title:String,
    val slug:String,
    val thumbnail:String,
    val user: User
)

data class QuizDetail(
    val id:Int,
    val title:String,
    val slug:String,
    val thumbnail:String,
    val user: User,
    val summary:String,
    val description:String,
    val createdAt:String,
    val updatedAt:String,
    val topic:Topic,
    val totalScore:Int,
    val collect:Boolean,
    val public:Boolean,
    val owner:Boolean
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

data class EditQuiz(
    val summary:String,
    val description:String,
    val title:String,
    val topicId: Int,
    val isPublic: Boolean,
    val quizId: Int,
    val slug:String
)

data class ChangeQuizThumbnail(
    val thumbnail: String,
    val quizId: Int
)