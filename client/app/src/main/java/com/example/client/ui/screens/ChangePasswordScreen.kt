package com.example.client.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.client.R
import com.example.client.ui.components.ButtonComponent
import com.example.client.ui.components.HeaderApp
import com.example.client.ui.components.PasswordFieldOutlined
import com.example.client.ui.components.TopBar
import com.example.client.ui.navigation.Routes
import com.example.client.ui.viewmodel.ChangePasswordViewModel
import com.example.client.ui.viewmodel.LoginViewModel
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState

@Composable
private fun ShowMessage(
        message: String,
) {
    Toast.makeText(
            LocalContext.current,
            message,
            Toast.LENGTH_LONG
    ).show()
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChangePasswordScreen (
    navController:NavController,
    changePasswordViewModel: ChangePasswordViewModel = hiltViewModel()
){
    val changePassword by changePasswordViewModel.changePassword.collectAsState()

    when(changePassword){
        is ResourceState.Success -> {
            ShowMessage((changePassword as ResourceState.Success<ApiResponse<Boolean>>).value.message)
        }
        is ResourceState.Error -> {
            (changePassword as ResourceState.Error).errorBody?.let { ShowMessage(it.message) }
        }
        else -> {

        }
    }

   Scaffold(
       topBar = {
           TopBar("Đổi Mật Khẩu",navController)
       },
       content = {
               Column (
                   modifier = Modifier
                       .fillMaxSize()
                       .padding(dimensionResource(id = R.dimen.padding_app))
                       .padding(it)
                       .verticalScroll(rememberScrollState()),
               ) {
                   HeaderApp(
                       painterResource(id = R.drawable.padlock),
                       stringResource(id = R.string.app_name),
                       stringResource(id = R.string.change_password)
                   )
                   PasswordFieldOutlined(
                       value = changePasswordViewModel.oldPassword,
                       onChangeValue = {
                           changePasswordViewModel.onChangeOldPassword(it)
                       },
                       label = stringResource(id = R.string.old_password),
                       painterResource = painterResource(id = R.drawable.password)
                   )
                   Spacer(
                       modifier = Modifier
                           .height(dimensionResource(id = R.dimen.space_app_normal))
                   )
                   PasswordFieldOutlined(
                       value = changePasswordViewModel.newPassword,
                       onChangeValue = {
                           changePasswordViewModel.onChangeNewPassword(it)
                       },
                       label = stringResource(id = R.string.new_password),
                       painterResource = painterResource(id = R.drawable.password)
                   )
                   Spacer(
                       modifier = Modifier
                           .height(dimensionResource(id = R.dimen.space_app_normal))
                   )
                   PasswordFieldOutlined(
                       value = changePasswordViewModel.confirmPassword,
                       onChangeValue = {
                           changePasswordViewModel.onChangeConfirmPassword(it)
                       },
                       label = stringResource(id = R.string.confirm_password),
                       painterResource = painterResource(id = R.drawable.password)
                   )
                   Spacer(
                       modifier = Modifier
                           .height(dimensionResource(id = R.dimen.space_app_normal))
                   )

                   ButtonComponent(
                       onClick = {

                       },
                       stringResource(id = R.string.change_password),
                       MaterialTheme.colorScheme.primary,
                       changePassword is ResourceState.Loading,
                       changePassword !is ResourceState.Loading
                   )
               }
       }
   )
}

