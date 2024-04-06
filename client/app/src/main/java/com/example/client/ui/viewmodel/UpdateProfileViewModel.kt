package com.example.client.ui.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client.model.ChangeAvatar
import com.example.client.model.ChangeDisplayName
import com.example.client.repositories.AuthRepository
import com.example.client.repositories.UserRepository
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel  @Inject constructor(
    private val userRepository: UserRepository
):ViewModel() {

    private val _displayName:MutableStateFlow<ResourceState<ApiResponse<String>>> = MutableStateFlow(ResourceState.Nothing)

    private val _changeAvatar:MutableStateFlow<ResourceState<ApiResponse<String>>> = MutableStateFlow(ResourceState.Nothing)
    val changeAvatar = _changeAvatar.asStateFlow()

    var displayName by mutableStateOf<String>("")
        private set

    var bottomSheetDisplayName by mutableStateOf<String>("")
        private set

    var isDisplayNameSelected by mutableStateOf(false)
        private set

    var avatar by mutableStateOf<String>("")
        private set

    var avatarPath by mutableStateOf<String?>(null)
        private set

    fun onChangeAvatar(newValue: String) {avatar = newValue}

    fun onChangeDisplayName(newValue:String) {displayName = newValue }
    fun onChangeBottomSheetDisplayName(newValue:String) {bottomSheetDisplayName = newValue}

    fun onChangeAvatarPath(newValue:String){ avatarPath = newValue}

    fun setIsDisplayNameSelected(newValue: Boolean) { isDisplayNameSelected = newValue }

    fun updateDisplayName(){
        val data = ChangeDisplayName(
            displayName = bottomSheetDisplayName
        )
        _displayName.value = ResourceState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response = userRepository.changeDisplayName(data)
            _displayName.value = response

            if(response is ResourceState.Success){
                displayName = data.displayName
            }
        }
    }

    fun updateAvatar(){
        val data = ChangeAvatar(
            avatar = avatarPath!!
        )
        _changeAvatar.value = ResourceState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response = userRepository.changeAvatar(data)
            _changeAvatar.value = response
            if(response is ResourceState.Success){
                avatar = response.value.data
            }
        }
    }

    fun resetChangeAvatarState(){
        _changeAvatar.value = ResourceState.Nothing
    }

    fun resetChangeDisplayName(){
        _displayName.value = ResourceState.Nothing
        bottomSheetDisplayName = displayName
    }
}