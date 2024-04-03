package com.example.client.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.client.R
import com.example.client.ui.components.*
import com.example.client.ui.navigation.Routes
import com.example.client.ui.viewmodel.VerifyViewModel
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun VerifyAccountScreen (
    email:String = "ABCASDASD@gmail.com",
    navController: NavController,
    verifyViewModel: VerifyViewModel = hiltViewModel()
){

    val verify by verifyViewModel.verify.collectAsState()

    verifyViewModel.onChangeEmail(email)


    if(verify is ResourceState.Error){
        (verify as ResourceState.Error).errorBody?.let { ShowMessage(it.message) }
    }

    if(verify is ResourceState.Success){
        ShowMessage((verify as ResourceState.Success<ApiResponse<Boolean>>).value.message)
        LaunchedEffect(Unit){
            navController.navigate(Routes.LOGIN_SCREEN)
        }
    }


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
            EmailDisplay(
                email
            )
            Spacer(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.space_app_small))
            )
            PasswordFieldOutlined(
                verifyViewModel.code,
                onChangeValue = {
                    verifyViewModel.onChangeToken(it)
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
                    verifyViewModel.verify()
                },
                stringResource(id = R.string.send),
                MaterialTheme.colorScheme.primary,
                verify is ResourceState.Loading,
                verify !is ResourceState.Loading
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
