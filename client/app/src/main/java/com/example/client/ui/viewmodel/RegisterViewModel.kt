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
}