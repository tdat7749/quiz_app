package com.example.client.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client.model.ChangePassword
import com.example.client.repositories.UserRepository
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
) :ViewModel() {

    private val _changePassword : MutableStateFlow<ResourceState<ApiResponse<Boolean>>> = MutableStateFlow(ResourceState.Nothing)
    val changePassword: MutableStateFlow<ResourceState<ApiResponse<Boolean>>> = _changePassword

    var validate by mutableStateOf<String?>(null)
        private set

    var oldPassword by mutableStateOf("")
        private set
    var newPassword by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set

    fun onChangeOldPassword(newValue: String) { oldPassword = newValue }
    fun onChangeNewPassword(newValue: String) { newPassword = newValue }
    fun onChangeConfirmPassword(newValue: String) { confirmPassword = newValue }


    fun changePassword(){
        val value = validated()
        if(value != null){
            validate = value
            return
        }
        val data = ChangePassword(
            newPassword,
            confirmPassword,
            oldPassword
        )
        viewModelScope.launch (Dispatchers.IO) {
            _changePassword.value = ResourceState.Loading
            val response = userRepository.changePassword(data)

            _changePassword.value = response
        }
    }

    fun validated():String?{
        if(newPassword.equals("") || newPassword == null){
            return "Không được để trống mật khẩu mới"
        }
        if(oldPassword.equals("") || oldPassword == null){
            return "Không được để trống mật khẩu hiện tại"
        }
        if(confirmPassword.equals("") || confirmPassword == null){
            return "Không được để trống xác nhận mật khẩu"
        }
        if(!newPassword.equals(confirmPassword)){
            return "Mật khẩu mới và mật khẩu xác nhận không trùng khớp"
        }
        return null
    }

    fun resetValidate(){
        validate = null
    }
}