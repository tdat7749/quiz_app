package com.example.client.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client.model.Verify
import com.example.client.repositories.AuthRepository
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class VerifyViewModel @Inject constructor(
    private val authRepository: AuthRepository
) :ViewModel() {

    private val _verify : MutableStateFlow<ResourceState<ApiResponse<Boolean>>> = MutableStateFlow(ResourceState.Nothing)
    val verify: MutableStateFlow<ResourceState<ApiResponse<Boolean>>> = _verify

    fun onChangeToken(newValue: String) {
        code = newValue
    }

    fun onChangeEmail(newValue: String) {
        email = newValue
    }


    var code by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    fun verify(){
        val data = Verify(
            code,
            email
        )

        viewModelScope.launch (Dispatchers.IO) {
            _verify.value = ResourceState.Loading
            val response =  authRepository.verify(data)
            _verify.value = response
        }
    }
}