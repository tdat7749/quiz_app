package com.example.client.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client.model.Quiz
import com.example.client.model.QuizDetail
import com.example.client.repositories.CollectRepository
import com.example.client.repositories.QuizRepository
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class QuizLandingViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val collectRepository: CollectRepository
): ViewModel() {

    private val _quiz : MutableStateFlow<ResourceState<ApiResponse<QuizDetail>>> = MutableStateFlow(ResourceState.Nothing)
    val quiz = _quiz.asStateFlow()

    private val _collect : MutableStateFlow<ResourceState<ApiResponse<Boolean>>> = MutableStateFlow(ResourceState.Nothing)
    val collect = _collect.asStateFlow()

    fun getQuizDetail(quizId: Int){
        _quiz.value = ResourceState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response = quizRepository.getQuizDetail(quizId)

            _quiz.value = response
        }
    }

    fun collectQuiz(quiz:QuizDetail){
        _collect.value = ResourceState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response = collectRepository.addToCollection(quiz.id)
            _collect.value = response

            if(response is ResourceState.Success){
                val updateQuiz = quiz.copy(collect = true)
                val updatedApiResponse = ApiResponse(data = updateQuiz, message = "")

                _quiz.value = ResourceState.Success(updatedApiResponse)
            }
        }
    }
}