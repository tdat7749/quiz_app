package com.example.client.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.client.R
import com.example.client.ui.components.ButtonComponent
import com.example.client.ui.components.HeaderApp
import com.example.client.ui.components.TextFieldOutlined
import com.example.client.ui.components.TopBar
import com.example.client.ui.navigation.Routes
import com.example.client.ui.viewmodel.SendEmailForgotViewModel
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SendEmailForgotScreen (
    navController: NavController,
    sendEmailForgotViewModel: SendEmailForgotViewModel = hiltViewModel()
) {
    val send by sendEmailForgotViewModel.send.collectAsState()

    if(send is ResourceState.Success){
        ShowMessage((send as ResourceState.Success<ApiResponse<Boolean>>).value.message)
        LaunchedEffect(Unit){
            navController.navigate("${Routes.FORGOT_PASSWORD_SCREEN}/${sendEmailForgotViewModel.email}")
        }
    }

    if(send is ResourceState.Error){
        (send as ResourceState.Error).errorBody?.let { ShowMessage(it.message) }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(id = R.string.forgot_password),
                navController = navController
            )
        },
        content = {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.padding_app))
                    .verticalScroll(rememberScrollState())
                    .background(Color.White)
            ){
                    HeaderApp(
                        painterResource(id = R.drawable.send_email),
                        stringResource(id = R.string.app_name),
                        stringResource(id = R.string.send_email_forgot)
                    )
                    Spacer(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.space_app_small))
                    )
                    TextFieldOutlined(
                        sendEmailForgotViewModel.email,
                        onChangeValue = {
                            sendEmailForgotViewModel.onChangeEmail(it)
                        },
                        stringResource(id = R.string.email),
                        painterResource(id = R.drawable.email)
                    )
                    Spacer(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.space_app_normal))
                    )
                    ButtonComponent(
                        onClick = {
                            sendEmailForgotViewModel.resendEmail()
                        },
                        stringResource(id = R.string.send),
                        MaterialTheme.colorScheme.primary,
                        send is ResourceState.Loading,
                        send !is ResourceState.Loading
                    )
                }
        }
    )
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
