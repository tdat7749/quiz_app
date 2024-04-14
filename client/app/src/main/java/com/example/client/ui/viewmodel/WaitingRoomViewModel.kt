package com.example.client.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.client.datasource.HistoryRankRoomDataSource
import com.example.client.datasource.RoomJoinedDataSource
import com.example.client.datasource.UsersInRoomDataSource
import com.example.client.model.HistoryAnswer
import com.example.client.model.HistoryRank
import com.example.client.model.Room
import com.example.client.model.User
import com.example.client.repositories.HistoryRepository
import com.example.client.repositories.RoomRepository
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WaitingRoomViewModel @Inject constructor(
    private val historyRepository: HistoryRepository,
    private val roomRepository: RoomRepository
): ViewModel() {

    private val _join: MutableStateFlow<ResourceState<ApiResponse<Room>>> = MutableStateFlow(ResourceState.Nothing)
    val join = _join.asStateFlow()

    private val _answer: MutableStateFlow<ResourceState<ApiResponse<List<HistoryAnswer>>>> = MutableStateFlow(ResourceState.Nothing)
    val answer = _answer.asStateFlow()

    private val _kick:MutableStateFlow<ResourceState<ApiResponse<Boolean>>> = MutableStateFlow(ResourceState.Nothing)
    val kick = _kick.asStateFlow()

    private val _keywordStateFlow = MutableStateFlow("")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun getHistoryRankRoom(roomId:Int): Flow<PagingData<HistoryRank>> = _keywordStateFlow
        .debounce(700)
        .distinctUntilChanged()
        .flatMapLatest {keyword ->
            Pager(PagingConfig(pageSize = 10,prefetchDistance = 1)){
                HistoryRankRoomDataSource(historyRepository,roomId)
            }.flow
        }.cachedIn(viewModelScope)

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun getUsersInRoom(roomId:Int): Flow<PagingData<User>> = _keywordStateFlow
        .debounce(700)
        .distinctUntilChanged()
        .flatMapLatest {keyword ->
            Pager(PagingConfig(pageSize = 10,prefetchDistance = 1)){
                UsersInRoomDataSource(roomId, roomRepository)
            }.flow
        }.cachedIn(viewModelScope)

    fun getRoomForParticipants(roomPin:String){
        _join.value = ResourceState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response = roomRepository.getRoomForParticipants(roomPin)
            _join.value = response
        }
    }

    fun getHistoryAnswer(roomId:Int){
        _answer.value = ResourceState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response = historyRepository.getHistoryAnswer(roomId)
            _answer.value = response
        }
    }

    fun kickUser(roomId:Int,userId:Int){
        _kick.value = ResourceState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response = roomRepository.kickUser(roomId,userId)
            _kick.value = response
            if(response is ResourceState.Success){

            }
        }
    }

    fun resetState(){
        _join.value = ResourceState.Nothing
    }

    fun resetKickState(){
        _kick.value = ResourceState.Nothing
    }
}