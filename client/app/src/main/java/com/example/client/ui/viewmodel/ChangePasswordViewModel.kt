package com.example.client.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ChangePasswordViewModel @Inject constructor(

) :ViewModel() {
    var oldPassword by mutableStateOf("")
        private set
    var newPassword by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set

    fun onChangeOldPassword(newValue: String) { oldPassword = newValue }
    fun onChangeNewPassword(newValue: String) { newPassword = newValue }
    fun onChangeConfirmPassword(newValue: String) { confirmPassword = newValue }
}