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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.client.R
import com.example.client.ui.components.*

@Composable
fun VerifyAccountScreen (){

    val token = remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)

    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_app))
        ){
            HeaderApp(
                painterResource(id = R.drawable.verify),
                stringResource(id = R.string.app_name),
                stringResource(id = R.string.verify)
            )
            TextFieldOutlined(
                token.value,
                onChangeValue = {
                    token.value = it
                },
                stringResource(id = R.string.verify_email),
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
fun VerifyAccountScreenPreview(){
    VerifyAccountScreen()
}
