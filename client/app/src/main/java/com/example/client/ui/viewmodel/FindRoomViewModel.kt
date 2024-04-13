package com.example.client.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client.model.JoinRoom
import com.example.client.model.Room
import com.example.client.repositories.RoomRepository
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindRoomViewModel @Inject constructor(
    private val roomRepository: RoomRepository
): ViewModel() {
    private val _join: MutableStateFlow<ResourceState<ApiResponse<JoinRoom>>> = MutableStateFlow(ResourceState.Nothing)
    val join = _join.asStateFlow()

    var roomPin by mutableStateOf<String>("")
        private set

    fun onChangeRoomPin(newValue:String) {roomPin = newValue}

    fun joinRoom(){
        _join.value = ResourceState.Nothing
        viewModelScope.launch(Dispatchers.IO) {
            val response = roomRepository.joinRoom(roomPin)
            _join.value = response
        }
    }

    fun resetState(){
        _join.value = ResourceState.Nothing
    }
}