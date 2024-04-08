package com.example.client.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client.model.CreateRoom
import com.example.client.model.EditRoom
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
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class EditRoomViewModel @Inject constructor(
    private val roomRepository: RoomRepository
): ViewModel() {

    private val _edit:MutableStateFlow<ResourceState<ApiResponse<Boolean>>> = MutableStateFlow(ResourceState.Nothing)
    val edit = _edit.asStateFlow()

    private val _room:MutableStateFlow<ResourceState<ApiResponse<Room>>> = MutableStateFlow(ResourceState.Nothing)
    val room = _room.asStateFlow()

    var timeStart by mutableStateOf<LocalDate?>(null)
        private set

    var timeStartClock by mutableStateOf<LocalTime?>(null)
        private set

    var timeEnd by  mutableStateOf<LocalDate?>(null)
        private set

    var timeEndClock by mutableStateOf<LocalTime?>(null)
        private set

    var roomName by mutableStateOf<String>("")
        private set

    var isClosed by mutableStateOf<Boolean>(false)
        private set

    fun onChangeTimeStart(newValue:LocalDate?){ timeStart = newValue}

    fun onChangeTimeStartClock(newValue:LocalTime?) {timeStartClock = newValue}

    fun onChangeTimeEnd(newValue:LocalDate?){ timeEnd = newValue}

    fun onChangeTimeEndClock(newValue:LocalTime?) {timeEndClock = newValue}

    fun onChangeRoomname(newValue:String) {roomName = newValue}
    fun onChangeClosed(newValue:Boolean) {isClosed = newValue}


    fun onEditRoom(roomId:Int){
        val data = EditRoom(
            roomId = roomId,
            timeStart = combineDateTime(timeStart,timeStartClock),
            timeEnd = combineDateTime(timeEnd,timeEndClock),
            roomName = roomName,
            isClosed = isClosed
        )

        _edit.value = ResourceState.Loading
        viewModelScope.launch (Dispatchers.IO) {
            val response = roomRepository.editRoom(data)
            _edit.value = response
        }
    }

    fun getRoom(id:Int){
        _room.value = ResourceState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response = roomRepository.getRoomDetail(id)
            _room.value = response
            if(response is ResourceState.Success){
                val room = response.value.data
                timeStart = room.timeStart?.toLocalDate()
                timeStartClock = room.timeStart?.toLocalTime()

                timeEnd = room.timeEnd?.toLocalDate()
                timeEndClock =  room.timeEnd?.toLocalTime()

                roomName = room.roomName
                isClosed = room.closed
            }
        }
    }





    private fun combineDateTime(localDate: LocalDate?,localTime:LocalTime?):LocalDateTime?{
        return if(localDate != null && localTime == null){
            localDate.atTime(LocalTime.of(0,0,0))
        }else if (localDate != null && localTime != null){
            localDate.atTime(localTime)
        }else {
            null
        }
    }

    fun resetState(){
        _edit.value = ResourceState.Nothing
    }
}