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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.client.R
import com.example.client.ui.components.*
import com.example.client.ui.navigation.Routes
import com.example.client.utils.ResourceState


@Composable
fun RegisterScreen(
    navController: NavController
){
    val userName = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val displayName = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_app)),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.space_app_extraLarge))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center

            ){
                Image(
                    painter = painterResource(id = R.drawable.choose),
                    contentDescription = stringResource(id = R.string.logo_description),
                    modifier = Modifier
                        .size(200.dp)
                )
            }
            Spacer(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.space_app_small))
            )
            HeadingBoldText(
                stringResource(id = R.string.app_name),
                TextAlign.Center,
                MaterialTheme.colorScheme.primary
            )
            Spacer(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.space_app_small))
            )
            NormalText(
                stringResource(id = R.string.register),
                TextAlign.Center,
                MaterialTheme.colorScheme.primary
            )
            Spacer(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.space_app_normal))
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
            TextFieldOutlined(
                email.value,
                onChangeValue = {
                    email.value = it
                },
                stringResource(id = R.string.email),
                painterResource(id = R.drawable.email)
            )
            Spacer(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.space_app_small))
            )
            TextFieldOutlined(
                displayName.value,
                onChangeValue = {
                  displayName.value = it
                },
                stringResource(id = R.string.display_name),
                painterResource(id = R.drawable.display_name)
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
                    .height(dimensionResource(id = R.dimen.space_app_small))
            )

            PasswordFieldOutlined(
                confirmPassword.value,
                onChangeValue = {
                    confirmPassword.value = it
                },
                stringResource(id = R.string.confirm_password),
                painterResource(id = R.drawable.password)
            )
            Spacer(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.space_app_normal))
            )

            ButtonComponent(
                onClick = {},
                stringResource(id = R.string.register),
                MaterialTheme.colorScheme.primary,
                false,
                false
            )

            Spacer(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.space_app_normal))
            )
            SmallText(
                stringResource(id = R.string.have_account),
                TextAlign.Start,
                MaterialTheme.colorScheme.onBackground,
                navController,
                Routes.LOGIN_SCREEN)
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview(){
    val navController = rememberNavController() // Tạo NavController giả
    val LocalNavController = compositionLocalOf<NavController> {
        error("No NavController provided")
    }
    CompositionLocalProvider(LocalNavController provides navController){
        RegisterScreen(LocalNavController.current)
    }
}