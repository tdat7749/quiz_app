package com.example.client.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client.model.CreateRoom
import com.example.client.model.Room
import com.example.client.repositories.RoomRepository
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateRoomViewModel @Inject constructor(
    private val roomRepository: RoomRepository
): ViewModel() {

    private val _create:MutableStateFlow<ResourceState<ApiResponse<Room>>> = MutableStateFlow(ResourceState.Nothing)
    val create = _create.asStateFlow()

    var timeStart by mutableStateOf<LocalDate?>(null)
        private set

    var timeEnd by  mutableStateOf<LocalDate?>(null)
        private set

    var roomName by mutableStateOf<String>("")
        private set

    fun onChangeTimeStart(newValue:LocalDate){ timeStart = newValue}

    fun onChangeTimeEnd(newValue:LocalDate){ timeEnd = newValue}

    fun onChangeRoomname(newValue:String) {roomName = newValue}


    fun onCreateRoom(quizId:Int){
        val data = CreateRoom(
            quizId = quizId,
            timeStart = timeStart,
            timeEnd = timeEnd,
            roomName = roomName
        )

        _create.value = ResourceState.Loading
        viewModelScope.launch (Dispatchers.IO) {
            val response = roomRepository.createRoom(data)
            _create.value = response
        }
    }

    fun resetState(){
        _create.value = ResourceState.Nothing
    }
}