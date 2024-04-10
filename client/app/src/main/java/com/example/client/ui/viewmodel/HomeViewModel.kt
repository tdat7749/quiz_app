package com.example.client.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import com.example.client.datasource.MyQuizzesDataSource
import com.example.client.datasource.MyRoomsDataSource
import com.example.client.datasource.RoomJoinedDataSource
import com.example.client.model.Quiz
import com.example.client.model.Room
import com.example.client.model.Topic
import com.example.client.model.User
import com.example.client.repositories.QuizRepository
import com.example.client.repositories.RoomRepository
import com.example.client.repositories.TopicRepository
import com.example.client.repositories.UserRepository
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import com.example.client.utils.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val topicRepository: TopicRepository,
    private val userRepository: UserRepository,
    private val quizRepository: QuizRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {

   

    private val _quizLatest : MutableStateFlow<ResourceState<ApiResponse<List<Quiz>>>> = MutableStateFlow(ResourceState.Nothing)
    val quizLatest = _quizLatest.asStateFlow()

    private val _quizTop10 : MutableStateFlow<ResourceState<ApiResponse<List<Quiz>>>> = MutableStateFlow(ResourceState.Nothing)
    val quizTop10 = _quizTop10.asStateFlow()

    private val _topics : MutableStateFlow<ResourceState<ApiResponse<List<Topic>>>> = MutableStateFlow(ResourceState.Nothing)
    val topics = _topics.asStateFlow()

    private val _user : MutableStateFlow<ResourceState<ApiResponse<User>>> = MutableStateFlow(ResourceState.Nothing)
    val user = _user.asStateFlow()

    private val _keywordStateFlow = MutableStateFlow("")

    init {
        getAllTopic()
        getMe()
        get10QuizLatest()
        getTop10QuizCollection()
    }



    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    fun getJoinedRooms(): Flow<PagingData<Room>> = _keywordStateFlow
        .debounce(700)
        .distinctUntilChanged()
        .flatMapLatest {keyword ->
            Pager(PagingConfig(pageSize = 10,prefetchDistance = 1)){
                RoomJoinedDataSource(roomRepository)
            }.flow
        }.cachedIn(viewModelScope)

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

   
    fun getTop10QuizCollection(){
        viewModelScope.launch(Dispatchers.IO) {
            _quizTop10.value = ResourceState.Loading
            val response = quizRepository.getTop10QuizCollection()
            if(response is ResourceState.Success){
                _quizTop10.value = response
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


}