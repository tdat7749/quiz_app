package com.example.client.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.client.ui.viewmodel.LoginViewModel
import com.example.client.R
import com.example.client.model.Login
import com.example.client.ui.components.*
import com.example.client.ui.navigation.Routes
import com.example.client.utils.ResourceState


@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel(),
){
    val auth by loginViewModel.auth.collectAsState()


    val userName = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val login = Login(userName.value,password.value)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_app))
            .verticalScroll(rememberScrollState()),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            HeaderApp(
                painterResource(id = R.drawable.choose),
                stringResource(id = R.string.app_name),
                stringResource(id = R.string.login)
            )
            TextFieldOutlined(
                userName.value,
                onChangeValue = {
                    userName.value = it
                },
                stringResource(id = R.string.user_name),
                painterResource(id = R.drawable.person)
            )
            Spacer(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.space_app_small))
            )
            PasswordFieldOutlined(
                password.value,
                onChangeValue = {
                    password.value = it
                },
                stringResource(id = R.string.password),
                painterResource(id = R.drawable.password)
            )
            Spacer(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.space_app_normal))
            )

            ButtonComponent(
                onClick = {
                  loginViewModel.login(login)
                },
                stringResource(id = R.string.login),
                MaterialTheme.colorScheme.primary,
                auth is ResourceState.Loading,
                auth is ResourceState.Loading
            )

            Spacer(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.space_app_normal))
            )
            SmallText(
                stringResource(id = R.string.havent_account),
                TextAlign.Start,
                MaterialTheme.colorScheme.onBackground,
                navController,
                Routes.REGISTER_SCREEN)
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview(){
    val navController = rememberNavController() // Tạo NavController giả
    val LocalNavController = compositionLocalOf<NavController> {
        error("No NavController provided")
    }
    CompositionLocalProvider(LocalNavController provides navController){
        LoginScreen(LocalNavController.current)
    }
}