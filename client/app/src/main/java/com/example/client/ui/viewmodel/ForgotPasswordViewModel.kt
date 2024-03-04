package com.example.client.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client.model.AuthToken
import com.example.client.model.ForgotPassword
import com.example.client.repositories.UserRepository
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _forgot : MutableStateFlow<ResourceState<ApiResponse<Boolean>>> = MutableStateFlow(ResourceState.Nothing)
    val forgot: MutableStateFlow<ResourceState<ApiResponse<Boolean>>> = _forgot

    var newPassword by  mutableStateOf("")
        private set

    var confirmPassword by  mutableStateOf("")
        private set

    var token by  mutableStateOf("")
        private set

    var email by  mutableStateOf("")
        private set

    fun onChangeNewPassword(newValue: String) { newPassword = newValue }
    fun onChangeConfirmPassword(newValue: String) { confirmPassword = newValue }
    fun onChangeToken(newValue: String) { token = newValue }
    fun onChangeEmail(newValue: String) { email = newValue }

    fun forgotPassword(){
        val data = ForgotPassword(
            token, email, newPassword, confirmPassword
        )

        viewModelScope.launch (Dispatchers.IO) {
            _forgot.value =  ResourceState.Loading
            val response = userRepository.forgotPassword(data)
            _forgot.value = response
        }
    }
}