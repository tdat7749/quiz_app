package com.example.client.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.client.R
import com.example.client.ui.components.*
import com.example.client.utils.ResourceState

@Composable
fun ForgotPasswordScreen(){

    val newPassword = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val token = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }

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
                newPassword.value,
                onChangeValue = {
                    newPassword.value = it
                },
                stringResource(id = R.string.new_password),
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
            PasswordFieldOutlined(
                token.value,
                onChangeValue = {
                    token.value = it
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

                },
                stringResource(id = R.string.send),
                MaterialTheme.colorScheme.primary,
                false,
                true
            )
        }
    }
}

@Preview
@Composable
fun ForgotPasswordScreenPreview(){
    ForgotPasswordScreen()
}