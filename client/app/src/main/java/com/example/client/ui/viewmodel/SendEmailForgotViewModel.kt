package com.example.client.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client.model.SendEmailForgot
import com.example.client.repositories.UserRepository
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendEmailForgotViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _send : MutableStateFlow<ResourceState<ApiResponse<Boolean>>> = MutableStateFlow(ResourceState.Nothing)
    val send: MutableStateFlow<ResourceState<ApiResponse<Boolean>>> = _send

    var email by mutableStateOf("")
        private set

    fun onChangeEmail(newValue: String) {
        email = newValue
    }


    fun resendEmail(){
        val data = SendEmailForgot(
            email
        )
        viewModelScope.launch (Dispatchers.IO) {
            _send.value = ResourceState.Loading
            val response = userRepository.sendEmailForgotPassword(data)
            _send.value = response
        }
    }
}