package com.example.client.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client.model.AuthToken
import com.example.client.model.Register
import com.example.client.repositories.AuthRepository
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import com.example.client.utils.Utilities
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _register : MutableStateFlow<ResourceState<ApiResponse<Boolean>>> = MutableStateFlow(ResourceState.Nothing)
    val register: MutableStateFlow<ResourceState<ApiResponse<Boolean>>> = _register

    var userName by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var displayName by mutableStateOf("")
        private set


    var validate by mutableStateOf<String?>(null)
        private set

    fun onChangeUserName(newValue: String) {
        userName = newValue
    }
    fun onChangePassword(newValue: String) {
        password = newValue
    }
    fun onChangeConfirmPassword(newValue: String) {
        confirmPassword = newValue
    }
    fun onChangeEmail(newValue: String) {
        email = newValue
    }
    fun onChangeDisplayName(newValue: String) {
        displayName = newValue
    }

    fun register(){
        val value = validated()
        if(value != null){
            validate = value
            return
        }
        val register = Register(
            userName,
            password,
            confirmPassword,
            email,
            displayName
        )
        viewModelScope.launch(Dispatchers.IO) {
            _register.value = ResourceState.Loading
            val response =  authRepository.register(register)

            _register.value = response
        }
    }

    fun validated():String?{
        if(userName.equals("") || userName == null){
            return "Không được để trống tài khoản"
        }
        if(password.equals("") || password == null){
            return "Không được để trống mật khẩu"
        }
        if(confirmPassword.equals("") || confirmPassword == null){
            return "Không được để trống xác nhận mật khẩu"
        }
        if(email.equals("") || email == null){
            return "Không được để trống email"
        }
        if(displayName.equals("") || displayName == null){
            return "Không được để trống tên hiển thị"
        }
        if(!Utilities.Companion.isValidEmail(email)){
            return "Email không hợp lệ"
        }

        return null
    }

    fun resetValidate(){
        validate = null
    }
}