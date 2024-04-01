package com.example.client.repositories

import android.util.Log
import com.example.client.model.CreateQuiz
import com.example.client.network.quiz.QuestionTypeService
import com.example.client.network.quiz.QuizService
import com.example.client.utils.Utilities
import okhttp3.MultipartBody
import javax.inject.Inject

class QuizRepository @Inject constructor(
    private val quizService: QuizService,
    private val questionTypeService: QuestionTypeService
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

    suspend fun  getAllQuestionType() = ApiHelper.safeCallApi {
        questionTypeService.getAllQuestionType()
    }

    suspend fun createQuiz(data: CreateQuiz) = ApiHelper.safeCallApi {

        val parts = mutableListOf<MultipartBody.Part>()

        parts.add(MultipartBody.Part.createFormData("summary", data.summary))
        parts.add(MultipartBody.Part.createFormData("description", data.description))
        parts.add(MultipartBody.Part.createFormData("title", data.title))
        parts.add(MultipartBody.Part.createFormData("topicId", data.topicId.toString()))
        parts.add(MultipartBody.Part.createFormData("isPublic", data.isPublic))
        parts.add(MultipartBody.Part.createFormData("slug", Utilities.Companion.slugify(data.title)))
        parts.add(Utilities.Companion.createFilePart("thumbnail", data.thumbnail))

        data.questions.forEachIndexed { index, questionDTO ->
            parts.add(MultipartBody.Part.createFormData("questions[$index].title", questionDTO.title))
            parts.add(MultipartBody.Part.createFormData("questions[$index].score", questionDTO.score.toString()))
            parts.add(MultipartBody.Part.createFormData("questions[$index].order", questionDTO.order.toString()))
            parts.add(MultipartBody.Part.createFormData("questions[$index].timeLimit", questionDTO.timeLimit.toString()))
            parts.add(MultipartBody.Part.createFormData("questions[$index].questionTypeId", questionDTO.questionType.id.toString()))
            if(questionDTO.thumbnail != null && questionDTO.thumbnail.trim() != ""){
                parts.add(Utilities.Companion.createFilePart("questions[$index].thumbnail", questionDTO.thumbnail))
            }


            questionDTO.answers.forEachIndexed { ansIndex, answerDTO ->
                parts.add(MultipartBody.Part.createFormData("questions[$index].answers[$ansIndex].title", answerDTO.title))
                parts.add(MultipartBody.Part.createFormData("questions[$index].answers[$ansIndex].isCorrect", answerDTO.isCorrect.toString()))
            }
        }

        quizService.createQuiz(parts)
    }
}