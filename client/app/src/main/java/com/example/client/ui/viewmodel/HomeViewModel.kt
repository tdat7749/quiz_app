package com.example.client.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client.model.Quiz
import com.example.client.model.Topic
import com.example.client.model.User
import com.example.client.repositories.QuizRepository
import com.example.client.repositories.TopicRepository
import com.example.client.repositories.UserRepository
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import com.example.client.utils.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val topicRepository: TopicRepository,
    private val userRepository: UserRepository,
    private val quizRepository: QuizRepository
) : ViewModel() {

    private val _topics : MutableStateFlow<ResourceState<ApiResponse<List<Topic>>>> = MutableStateFlow(ResourceState.Nothing)
    val topics = _topics.asStateFlow()

    private val _user : MutableStateFlow<ResourceState<ApiResponse<User>>> = MutableStateFlow(ResourceState.Nothing)
    val user = _user.asStateFlow()

    private val _quizLatest : MutableStateFlow<ResourceState<ApiResponse<List<Quiz>>>> = MutableStateFlow(ResourceState.Nothing)
    val quizLatest = _quizLatest.asStateFlow()

    private val _quizTop10 : MutableStateFlow<ResourceState<ApiResponse<List<Quiz>>>> = MutableStateFlow(ResourceState.Nothing)
    val quizTop10 = _quizTop10.asStateFlow()

    init {
        getAllTopic()
        getMe()
        get10QuizLatest()
        getTop10QuizCollection()
    }

    fun getAllTopic(){
        viewModelScope.launch(Dispatchers.IO){
            _topics.value = ResourceState.Loading
            val response = topicRepository.getAllTopic()

            if(response is ResourceState.Success){
                _topics.value = response
            }
        }
    }

    fun getMe(){
        viewModelScope.launch(Dispatchers.IO) {
            _user.value = ResourceState.Loading
            val userResponse = userRepository.getMe()
            if(userResponse is ResourceState.Success){
                SharedPreferencesManager.saveUser(userResponse.value.data)
                _user.value = userResponse
            }
        }
    }

    fun get10QuizLatest(){
        viewModelScope.launch(Dispatchers.IO) {
            _quizLatest.value = ResourceState.Loading
            val response = quizRepository.get10QuizLatest()
            if(response is ResourceState.Success){
                _quizLatest.value = response
            }
        }
    }

    fun getTop10QuizCollection(){
        viewModelScope.launch(Dispatchers.IO) {
            _quizTop10.value = ResourceState.Loading
            val response = quizRepository.getTop10QuizCollection()
            if(response is ResourceState.Success){
                _quizTop10.value = response
            }
        }
    }

}