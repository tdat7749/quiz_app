package com.example.client.model

import java.util.*

data class CreateHistory(
    val totalCorrect:Int,
    val startedAt: String,
    val finishedAt: String,
    val totalScore:Int,
    val quizId:Int?,
    val roomId:Int?,
    val historyAnswers:List<CreateHistoryAnswer>
)


data class CreateHistoryAnswer(
    val questionId:Int,
    val isCorrect:Boolean,
    val answerId:Int?
)

data class Answered(
    val questionId:Int,
    val answerId:Int?,
    val isCorrect:Boolean,
    val score:Int
)