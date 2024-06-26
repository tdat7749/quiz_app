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


data class HistoryRank(
    val totalCorrect:Int,
    val totalScore: Int,
    val user:User,
)

data class HistoryAnswer(
    val id:Int,
    val correct:Boolean,
    val question:Question,
    val answer:Answer?
)