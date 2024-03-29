package com.example.client.repositories

import com.example.client.network.quiz.QuizService
import javax.inject.Inject

class QuizRepository @Inject constructor(
    private val quizService: QuizService
){
    suspend fun getPublicQuiz(keyword:String,pageIndex:Int,sortBy:String,topicId:Int) = ApiHelper.safeCallApi {
        quizService.getPublicQuizzes(topicId,keyword, pageIndex, sortBy)
    }

    suspend fun get10QuizLatest() = ApiHelper.safeCallApi {
        quizService.get10QuizLatest()
    }

    suspend fun getTop10QuizCollection() = ApiHelper.safeCallApi {
        quizService.getTop10QuizCollection()
    }
}