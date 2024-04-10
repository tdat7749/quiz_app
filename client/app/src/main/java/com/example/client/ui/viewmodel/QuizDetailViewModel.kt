package com.example.client.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client.model.Quiz
import com.example.client.model.QuizDetail
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
class QuizDetailViewModel @Inject constructor(
    private val quizRepository: QuizRepository
):ViewModel() {
    private val _detail:MutableStateFlow<ResourceState<ApiResponse<QuizDetail>>> = MutableStateFlow(ResourceState.Nothing)
    val detail = _detail.asStateFlow()


    fun getDetail(id:Int){
        _detail.value = ResourceState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response = quizRepository.getQuizDetail(id)

            _detail.value = response
        }
    }
}