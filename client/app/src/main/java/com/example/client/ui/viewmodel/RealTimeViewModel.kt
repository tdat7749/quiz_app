package com.example.client.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client.model.RoomRealTime
import com.example.client.repositories.RoomRepository
import com.example.client.utils.SharedPreferencesManager
import com.google.firebase.database.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RealTimeViewModel @Inject constructor(
    private val roomRepository: RoomRepository
):ViewModel() {
    private val dbRef = FirebaseDatabase.getInstance().getReference("rooms")

    private val _roomDetails:MutableStateFlow<RoomRealTime?> = MutableStateFlow(null)
    val roomDetail = _roomDetails.asStateFlow()

    private val _isHost:MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isHost = _isHost.asStateFlow()

    private val _userId:MutableStateFlow<String> = MutableStateFlow("")
    val userId = _userId.asStateFlow()

    private val _isLoading:MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    fun listenToRoomChanges(roomId: String) {
        _isLoading.value = true
        dbRef.child(roomId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val room = snapshot.getValue(RoomRealTime::class.java)
                _roomDetails.value = room

                val currentUser = SharedPreferencesManager.getUser()
                if(currentUser != null && currentUser.id == room?.host?.id){
                    _isHost.value = true
                    _userId.value = currentUser.id.toString()
                }

                _isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                // Log error or handle cancellation

                _isLoading.value = false
            }
        })
    }

    fun leaveRoom(roomId:Int){
        try {
            viewModelScope.launch {
                roomRepository.leaveRoom(roomId)
            }
        } catch (e: Exception) {
            Log.e("Error", "Error in leaveRoom: ${e.message}")
        }
    }
}