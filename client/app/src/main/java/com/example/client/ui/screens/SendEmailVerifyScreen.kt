package com.example.client.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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
import com.example.client.ui.components.*
import com.example.client.ui.navigation.Routes
import com.example.client.ui.viewmodel.SendEmailVerifyViewModel
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendEmailVerifyScreen(
    navController: NavController,
    sendEmailVerifyViewModel: SendEmailVerifyViewModel = hiltViewModel()
) {

    val send by sendEmailVerifyViewModel.send.collectAsState()

    if(send is ResourceState.Error){
        (send as ResourceState.Error).errorBody?.let { ShowMessage(it.message) }
    }

    if(send is ResourceState.Success){
        ShowMessage((send as ResourceState.Success<ApiResponse<Boolean>>).value.message)
        LaunchedEffect(Unit){
            navController.navigate("${Routes.VERIFY_SCREEN}/${sendEmailVerifyViewModel.email}")
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(id = R.string.verify),
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
                        stringResource(id = R.string.resend_email_verify)
                    )
                    Spacer(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.space_app_small))
                    )
                    TextFieldOutlined(
                        sendEmailVerifyViewModel.email,
                        onChangeValue = {
                            sendEmailVerifyViewModel.onChangeEmail(it)
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
                            sendEmailVerifyViewModel.resendEmail()
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
