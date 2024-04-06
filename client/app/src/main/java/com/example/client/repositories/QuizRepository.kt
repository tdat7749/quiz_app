package com.example.client.repositories

import com.example.client.model.*
import com.example.client.network.quiz.QuestionService
import com.example.client.network.quiz.QuestionTypeService
import com.example.client.network.quiz.QuizService
import com.example.client.utils.Utilities
import okhttp3.MultipartBody
import javax.inject.Inject

class QuizRepository @Inject constructor(
    private val quizService: QuizService,
    private val questionTypeService: QuestionTypeService,
    private val questionService:QuestionService
){
    suspend fun getPublicQuiz(keyword:String,pageIndex:Int,sortBy:String,topicId:Int) = ApiHelper.safeCallApi {
        quizService.getPublicQuizzes(topicId,keyword, pageIndex, sortBy)
    }

    suspend fun getMyListCollection(keyword:String,pageIndex:Int,sortBy:String) = ApiHelper.safeCallApi {
        quizService.getMyListCollection(keyword, pageIndex, sortBy)
    }

    suspend fun getMyQuizzes(keyword:String,pageIndex:Int,sortBy:String) = ApiHelper.safeCallApi {
        quizService.getMyQuizzes(keyword, pageIndex, sortBy)
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

    suspend fun getQuizDetail(quizId:Int) = ApiHelper.safeCallApi {
        quizService.getQuizDetail(quizId)
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

    suspend fun getListQuestionByQuizId(quizId:Int) = ApiHelper.safeCallApi {
        questionService.getListQuestionByQuizId(quizId)
    }

    suspend fun changeThumbnail(data: ChangeQuizThumbnail) = ApiHelper.safeCallApi {
        val thumbnail = mutableListOf<MultipartBody.Part>()
        thumbnail.add(Utilities.Companion.createFilePart("thumbnail", data.thumbnail))
        thumbnail.add(MultipartBody.Part.createFormData("quizId", data.quizId.toString()))

        quizService.changeThumbnail(thumbnail)
    }

    suspend fun editQuiz(data:EditQuiz) = ApiHelper.safeCallApi {
        quizService.editQuiz(data)
    }

    suspend fun editQuestion(data:EditQuestion) = ApiHelper.safeCallApi {
        questionService.editQuestion(data)
    }

    suspend fun createQuestion(data:CreateQuestion,quizId:Int) = ApiHelper.safeCallApi {
        val parts = mutableListOf<MultipartBody.Part>()

        parts.add(MultipartBody.Part.createFormData("title", data.title))
        parts.add(MultipartBody.Part.createFormData("score", data.score.toString()))
        parts.add(MultipartBody.Part.createFormData("order", data.order.toString()))
        parts.add(MultipartBody.Part.createFormData("timeLimit", data.timeLimit.toString()))
        parts.add(MultipartBody.Part.createFormData("questionTypeId", data.questionType.id.toString()))

        if(data.thumbnail != null && data.thumbnail.trim() != ""){
            parts.add(Utilities.Companion.createFilePart("thumbnail", data.thumbnail))
        }

        data.answers.forEachIndexed { ansIndex, answerDTO ->
            parts.add(MultipartBody.Part.createFormData("answers[$ansIndex].title", answerDTO.title))
            parts.add(MultipartBody.Part.createFormData("answers[$ansIndex].isCorrect", answerDTO.isCorrect.toString()))
        }

        questionService.createQuestion(parts,quizId)
    }

    suspend fun editQuestionThumbnail(data:EditQuestionThumbnail) = ApiHelper.safeCallApi {
        val thumbnail = mutableListOf<MultipartBody.Part>()
        thumbnail.add(Utilities.Companion.createFilePart("thumbnail", data.thumbnail))
        thumbnail.add(MultipartBody.Part.createFormData("quizId", data.quizId.toString()))
        thumbnail.add(MultipartBody.Part.createFormData("questionId", data.questionId.toString()))

        questionService.editQuestionThumbnail(thumbnail)
    }
}