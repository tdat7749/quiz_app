package com.example.client.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client.model.UserDetail
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
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    private val _me: MutableStateFlow<ResourceState<ApiResponse<UserDetail>>> = MutableStateFlow(ResourceState.Nothing)
    val me = _me.asStateFlow()


//    init{
//        getMeDetail()
//    }

    fun getMeDetail(){
        _me.value = ResourceState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response = userRepository.getMeDetail()
            _me.value = response
        }
    }

    fun resetState(){
        _me.value = ResourceState.Nothing
    }
}