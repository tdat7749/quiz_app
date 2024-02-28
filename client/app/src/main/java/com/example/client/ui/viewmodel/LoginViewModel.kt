package com.example.client.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client.utils.ApiResponse
import com.example.client.model.AuthToken
import com.example.client.model.Login
import com.example.client.repositories.AuthRepository
import com.example.client.utils.AppConstants
import com.example.client.utils.ResourceState
import com.example.client.utils.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _auth : MutableStateFlow<ResourceState<ApiResponse<AuthToken>>> = MutableStateFlow(ResourceState.Success(null))
    val auth: MutableStateFlow<ResourceState<ApiResponse<AuthToken>>> = _auth


    fun login(login:Login){
        viewModelScope.launch(Dispatchers.IO) {
            _auth.value = ResourceState.Loading
            val response = authRepository.login(login)
             _auth.value = response
            if(response is ResourceState.Success){
                val accessToken = response.value?.data?.accessToken
                val sharedPreferences = SharedPreferencesManager.getSharedPreferences()
                sharedPreferences.edit().putString(AppConstants.ACCESS_TOKEN,accessToken).apply()
            }
        }
    }
}