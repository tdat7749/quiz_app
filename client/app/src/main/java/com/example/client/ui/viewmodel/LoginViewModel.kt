package com.example.client.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.client.utils.ApiResponse
import com.example.client.model.AuthToken
import com.example.client.model.Login
import com.example.client.model.User
import com.example.client.repositories.AuthRepository
import com.example.client.repositories.UserRepository
import com.example.client.ui.navigation.Routes
import com.example.client.utils.ResourceState
import com.example.client.utils.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _auth : MutableStateFlow<ResourceState<ApiResponse<AuthToken>>> = MutableStateFlow(ResourceState.Nothing)
    val auth = _auth.asStateFlow()


    var userName by  mutableStateOf("")
        private set

    var password by  mutableStateOf("")
        private set

    fun onChangeUserName(newValue: String) { userName = newValue }
    fun onChangePassword(newValue: String) { password = newValue }


    fun login(){
        val login:Login = Login(
            userName,
            password
        )
        viewModelScope.launch(Dispatchers.IO) {
            _auth.value = ResourceState.Loading
            val response = authRepository.login(login)
             _auth.value = response
            if(response is ResourceState.Success){
                with(SharedPreferencesManager){
                    saveToken(response.value.data.accessToken,response.value.data.refreshToken)
                }
            }
        }
    }

    fun checkLogin(navController: NavController){
        val accessToken = SharedPreferencesManager.getAccessToken()
        if(accessToken != null){
            viewModelScope.launch(Dispatchers.Main) {
                val response =  userRepository.getMe()
                if(response is ResourceState.Success){
                    navController.navigate(Routes.HOME_SCREEN)
                    val user = SharedPreferencesManager.getUser(User::class.java)
                    if(user == null){
                        with(SharedPreferencesManager){
                            saveUser(response.value.data)
                        }
                    }
                }
            }
        }
    }

    fun resetState() {
        _auth.value = ResourceState.Nothing
    }
}