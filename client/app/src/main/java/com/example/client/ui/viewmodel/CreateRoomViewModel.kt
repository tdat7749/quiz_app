package com.example.client.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
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
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class CreateRoomViewModel @Inject constructor(
    private val roomRepository: RoomRepository
): ViewModel() {

    private val _create:MutableStateFlow<ResourceState<ApiResponse<Room>>> = MutableStateFlow(ResourceState.Nothing)
    val create = _create.asStateFlow()

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

    var maxUser by mutableStateOf<Int>(40)
        private set

    var isPlayAgain by mutableStateOf<Boolean>(false)
        private set

    fun onChangeTimeStart(newValue:LocalDate?){ timeStart = newValue}

    fun onChangeTimeStartClock(newValue:LocalTime?) {timeStartClock = newValue}

    fun onChangeTimeEnd(newValue:LocalDate?){ timeEnd = newValue}

    fun onChangeTimeEndClock(newValue:LocalTime?) {timeEndClock = newValue}

    fun onChangeRoomname(newValue:String) {roomName = newValue}

    fun onChangeMaxUser(newValue:Int) {maxUser = newValue}

    fun onChangePlayAgain(newValue:Boolean) {isPlayAgain = newValue}


    fun onCreateRoom(quizId:Int){
        val data = CreateRoom(
            quizId = quizId,
            timeStart = combineDateTime(timeStart,timeStartClock),
            timeEnd = combineDateTime(timeEnd,timeEndClock),
            roomName = roomName,
            maxUser = maxUser,
            isPlayAgain = isPlayAgain
        )

        _create.value = ResourceState.Loading
        viewModelScope.launch (Dispatchers.IO) {
            val response = roomRepository.createRoom(data)
            _create.value = response
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
        _create.value = ResourceState.Nothing
    }
}