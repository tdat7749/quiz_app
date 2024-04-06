package com.example.client.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class RoomDetailViewModel @Inject constructor(
    private val roomRepository: RoomRepository
):ViewModel() {
    private val _room: MutableStateFlow<ResourceState<ApiResponse<Room>>> = MutableStateFlow(ResourceState.Nothing)
    val room = _room.asStateFlow()

    fun getRoomDetail(roomId:Int){
        _room.value = ResourceState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            val response = roomRepository.getRoomDetail(roomId)

            _room.value = response
        }
    }
}