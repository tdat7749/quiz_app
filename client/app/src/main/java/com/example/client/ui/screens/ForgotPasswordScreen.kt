package com.example.client.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.client.R
import com.example.client.model.AuthToken
import com.example.client.ui.components.*
import com.example.client.ui.navigation.Routes
import com.example.client.ui.viewmodel.ForgotPasswordViewModel
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    forgotPasswordViewModel: ForgotPasswordViewModel = hiltViewModel()
){

    val forgot by forgotPasswordViewModel.forgot.collectAsState()

    when(forgot){
        is ResourceState.Success -> {
            ShowMessage((forgot as ResourceState.Success<ApiResponse<Boolean>>).value.message)
            navController.navigate(Routes.LOGIN_SCREEN)
        }
        is ResourceState.Error -> {
            (forgot as ResourceState.Error).errorBody?.let { ShowMessage(it.message) }
        }
        else -> {

        }
    }

    Surface (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(dimensionResource(id = R.dimen.padding_app))
    ){
        Column (
            modifier = Modifier
                .fillMaxSize()
        ) {
            HeaderApp(
                painterResource(id = R.drawable.padlock),
                stringResource(id = R.string.app_name),
                stringResource(id = R.string.forgot_password)
            )
            PasswordFieldOutlined(
                forgotPasswordViewModel.newPassword,
                onChangeValue = {
                    forgotPasswordViewModel.onChangeNewPassword(it)
                },
                stringResource(id = R.string.new_password),
                painterResource(id = R.drawable.password)
            )
            Spacer(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.space_app_small))
            )
            PasswordFieldOutlined(
                forgotPasswordViewModel.confirmPassword,
                onChangeValue = {
                    forgotPasswordViewModel.onChangeConfirmPassword(it)
                },
                stringResource(id = R.string.confirm_password),
                painterResource(id = R.drawable.password)
            )
            Spacer(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.space_app_small))
            )
            TextFieldOutlined(
                forgotPasswordViewModel.email,
                onChangeValue = {
                    forgotPasswordViewModel.onChangeEmail(it)
                },
                stringResource(id = R.string.email),
                painterResource(id = R.drawable.email)
            )
            Spacer(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.space_app_small))
            )
            PasswordFieldOutlined(
                forgotPasswordViewModel.token,
                onChangeValue = {
                    forgotPasswordViewModel.onChangeToken(it)
                },
                stringResource(id = R.string.token),
                painterResource(id = R.drawable.token)
            )
            Spacer(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.space_app_normal))
            )
            ButtonComponent(
                onClick = {
                    forgotPasswordViewModel.forgotPassword()
                },
                stringResource(id = R.string.send),
                MaterialTheme.colorScheme.primary,
                forgot is ResourceState.Loading,
                forgot !is ResourceState.Loading
            )
        }
    }
}

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
