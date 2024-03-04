package com.example.client.ui.screens

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.client.R
import com.example.client.ui.components.*
import com.example.client.ui.navigation.Routes
import com.example.client.ui.viewmodel.RegisterViewModel
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState


@Composable
fun RegisterScreen(
    navController: NavController,
    registerViewModel: RegisterViewModel = hiltViewModel()
){
    val register by registerViewModel.register.collectAsState()

    if(register is ResourceState.Success){
        ShowMessage((register as ResourceState.Success<ApiResponse<Boolean>>).value.message)
        navController.navigate(Routes.LOGIN_SCREEN)
    }
    if(register is ResourceState.Error){
        (register as ResourceState.Error).errorBody?.let { ShowMessage(it.message) }
    }

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
                stringResource(id = R.string.register)
            )
            TextFieldOutlined(
                registerViewModel.userName,
                onChangeValue = {
                    registerViewModel.onChangeUserName(it)
                },
                stringResource(id = R.string.user_name),
                painterResource(id = R.drawable.person)
            )
            Spacer(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.space_app_small))
            )
            TextFieldOutlined(
                registerViewModel.email,
                onChangeValue = {
                    registerViewModel.onChangeEmail(it)
                },
                stringResource(id = R.string.email),
                painterResource(id = R.drawable.email)
            )
            Spacer(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.space_app_small))
            )
            TextFieldOutlined(
                registerViewModel.displayName,
                onChangeValue = {
                    registerViewModel.onChangeDisplayName(it)
                },
                stringResource(id = R.string.display_name),
                painterResource(id = R.drawable.display_name)
            )
            Spacer(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.space_app_small))
            )
            PasswordFieldOutlined(
                registerViewModel.password,
                onChangeValue = {
                    registerViewModel.onChangePassword(it)
                },
                stringResource(id = R.string.password),
                painterResource(id = R.drawable.password)
            )
            Spacer(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.space_app_small))
            )

            PasswordFieldOutlined(
                registerViewModel.confirmPassword,
                onChangeValue = {
                    registerViewModel.onChangeConfirmPassword(it)
                },
                stringResource(id = R.string.confirm_password),
                painterResource(id = R.drawable.password)
            )
            Spacer(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.space_app_normal))
            )

            ButtonComponent(
                onClick = {
                    registerViewModel.register()
                },
                stringResource(id = R.string.register),
                MaterialTheme.colorScheme.primary,
                register is ResourceState.Loading,
                register !is ResourceState.Loading
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