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
import com.example.client.model.LoginWithGoogle
import com.example.client.model.User
import com.example.client.repositories.AuthRepository
import com.example.client.repositories.UserRepository
import com.example.client.ui.navigation.Routes
import com.example.client.utils.ResourceState
import com.example.client.utils.SharedPreferencesManager
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _auth : MutableStateFlow<ResourceState<ApiResponse<AuthToken>>> = MutableStateFlow(ResourceState.Nothing)
    val auth = _auth.asStateFlow()

    private val _authGoogle : MutableStateFlow<ResourceState<ApiResponse<AuthToken>>> = MutableStateFlow(ResourceState.Nothing)
    val authGoogle = _authGoogle.asStateFlow()

    private val _checkLogin: MutableStateFlow<ResourceState<ApiResponse<User>>> = MutableStateFlow(ResourceState.Nothing)
    val checkLogin = _checkLogin.asStateFlow()

    val _googleState:MutableStateFlow<ResourceState<AuthResult>> = MutableStateFlow(ResourceState.Nothing)
    val googleState = _googleState.asStateFlow()

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



    fun googleSignIn(credential: AuthCredential){
        _googleState.value = ResourceState.Loading
        _authGoogle.value = ResourceState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response = authRepository.googleSignIn(credential)
            _googleState.value = response

            if(response is ResourceState.Success){
                val tokenResponse = authRepository.loginWithGoogle(response.value.user?.getIdToken(false)?.await()?.token!!)
                _authGoogle.value = tokenResponse
                if(tokenResponse is ResourceState.Success){
                    with(SharedPreferencesManager){
                        saveToken(tokenResponse.value.data.accessToken,tokenResponse.value.data.refreshToken)
                    }
                    authRepository.googleLogout()
                }else{
                    authRepository.googleLogout()
                }
            }else if (response is ResourceState.Error){
                _authGoogle.value = ResourceState.Nothing
            }
        }
    }


fun checkLogin(navController: NavController){
        val accessToken = SharedPreferencesManager.getAccessToken()
        if(accessToken != null){
            _checkLogin.value = ResourceState.Loading
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
                _checkLogin.value = response
            }
        }
    }

    fun resetState() {
        _checkLogin.value = ResourceState.Nothing
        _auth.value = ResourceState.Nothing
    }

    fun resetGoogleState(){
        _checkLogin.value = ResourceState.Nothing
        _googleState.value = ResourceState.Nothing
    }

    fun resetAuthGoogleState(){
        _checkLogin.value = ResourceState.Nothing
        _authGoogle.value = ResourceState.Nothing
    }
}