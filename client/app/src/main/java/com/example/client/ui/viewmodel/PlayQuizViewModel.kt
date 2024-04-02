package com.example.client.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client.model.*
import com.example.client.repositories.HistoryRepository
import com.example.client.repositories.QuizRepository
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import com.example.client.utils.Utilities
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@HiltViewModel
class PlayQuizViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val historyRepository: HistoryRepository
): ViewModel() {

    private val _startQuizState : MutableStateFlow<ResourceState<ApiResponse<List<QuestionDetail>>>> = MutableStateFlow(ResourceState.Nothing)
    val startQuizState = _startQuizState.asStateFlow()

    private val _saveResult : MutableStateFlow<ResourceState<ApiResponse<Boolean>>> = MutableStateFlow(ResourceState.Nothing)
    val saveResult = _saveResult.asStateFlow()


    var timeStart by mutableStateOf(Date())
        private set

    var questionIndex by mutableStateOf(0)
        private set

    private var totalQuestion by mutableStateOf(-1)
        private set

    var totalCorrect by mutableStateOf(0)
        private set
    var totalAnswered by mutableStateOf(0)
        private set
    var totalScore by mutableStateOf(0)
        private set

    fun isLastQuestion(): Boolean = questionIndex == totalQuestion - 1

    private var answers: ArrayList<Answered> = arrayListOf()

    fun addSelectAnswer(answer: Answered){
        answers.add(answer)
    }

    fun goNextQuestion() {
        if (!isLastQuestion()) {
            questionIndex++
        }
    }

    fun finishQuiz(quizId:Int? = null,roomId:Int? = null){ // finished chuyyển hướng sang trang QUIZ RESULT nhớ set popUpTo(0) để không cho back về
        val listHistoryAnswer:ArrayList<CreateHistoryAnswer> = arrayListOf()
        answers.forEach{
            totalScore += it.score
            if(it.isCorrect){
                totalCorrect += 1
            }
            if(it.answerId != null && it.answerId != -1){
                totalAnswered += 1
            }
            val data = CreateHistoryAnswer(
                it.questionId,
                isCorrect = it.isCorrect,
                answerId = it.answerId
            )
            listHistoryAnswer.add(data)
        }

        val data = CreateHistory(
            totalCorrect = totalCorrect,
            totalScore = totalScore,
            historyAnswers = listHistoryAnswer,
            finishedAt = Utilities.Companion.formatDate(Date()),
            startedAt = Utilities.Companion.formatDate(timeStart),
            quizId = if(roomId == null) quizId else null,
            roomId = roomId
        )

        saveResult(data)
    }

    private fun saveResult(data:CreateHistory){
        _saveResult.value = ResourceState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            // viết code save result, save xong nhớ xóa
            val response = historyRepository.createHistory(data)

            _saveResult.value = response
        }
    }

    fun getListQuestionByQuizId(quizId:Int){
        _startQuizState.value = ResourceState.Loading

        viewModelScope.launch (Dispatchers.IO) {
            val response = quizRepository.getListQuestionByQuizId(quizId)
            _startQuizState.value = response

            if(response is ResourceState.Success){
                totalQuestion = response.value.data.size
                timeStart = Date()
            }
        }
    }

    fun resetState(){
        totalCorrect = 0
        totalScore = 0
        totalAnswered = 0
        answers.clear()
        questionIndex = 0
    }

    fun resetFlowState(){
        _startQuizState.value = ResourceState.Nothing
        _saveResult.value = ResourceState.Nothing
    }
}

