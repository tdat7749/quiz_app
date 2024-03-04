package com.example.client.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client.model.Topic
import com.example.client.model.User
import com.example.client.repositories.TopicRepository
import com.example.client.repositories.UserRepository
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import com.example.client.utils.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val topicRepository: TopicRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _topics : MutableStateFlow<ResourceState<ApiResponse<List<Topic>>>> = MutableStateFlow(ResourceState.Nothing)
    val topics: MutableStateFlow<ResourceState<ApiResponse<List<Topic>>>> = _topics


    fun getAllTopic(){
        viewModelScope.launch(Dispatchers.IO){
            _topics.value = ResourceState.Loading
            val response = topicRepository.getAllTopic()

            if(response is ResourceState.Success){
                _topics.value = response
            }
        }
    }

    fun getMe():User{
        return SharedPreferencesManager.getUser(User::class.java)!!
    }
}