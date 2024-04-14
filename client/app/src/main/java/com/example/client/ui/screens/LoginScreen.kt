package com.example.client.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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
import com.example.client.ui.viewmodel.LoginViewModel
import com.example.client.R
import com.example.client.model.AuthToken
import com.example.client.model.Login
import com.example.client.ui.components.*
import com.example.client.ui.navigation.Routes
import com.example.client.ui.navigation.Routes.HOME_SCREEN
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel(),
){
    val auth by loginViewModel.auth.collectAsState()
    val checkLogin by loginViewModel.checkLogin.collectAsState()
    val googleState by loginViewModel.googleState.collectAsState()
    val authGoogle by loginViewModel.authGoogle.collectAsState()

    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val result = account.getResult(ApiException::class.java)
                val credentials = GoogleAuthProvider.getCredential(result.idToken, null)
                loginViewModel.googleSignIn(credentials)
            } catch (it: ApiException) {
                print(it)
            }
        }

    LaunchedEffect(Unit){
        loginViewModel.checkLogin(navController)
    }

    if(loginViewModel.validate != null){
        ShowMessage(loginViewModel.validate!!)
    }

    DisposableEffect(Unit){
        onDispose {
            loginViewModel.resetValidate()
        }
    }

    BackHandler(enabled = true){

    }

    when(authGoogle){
        is ResourceState.Success -> {
            ShowMessage((authGoogle as ResourceState.Success<ApiResponse<AuthToken>>).value.message)
            LaunchedEffect(Unit){
                navController.navigate(Routes.HOME_SCREEN)
            }
        }
        is ResourceState.Error -> {
            (authGoogle as ResourceState.Error).errorBody?.let { ShowMessage(it.message) { loginViewModel.resetAuthGoogleState() } }
        }
        else -> {

        }
    }

    when{
        auth is ResourceState.Error -> {
            (auth as ResourceState.Error).errorBody?.let { ShowMessage(it.message) { loginViewModel.resetState() } }
        }
        auth is ResourceState.Success -> {
            ShowMessage((auth as ResourceState.Success<ApiResponse<AuthToken>>).value.message)
            LaunchedEffect(Unit){
                navController.navigate(Routes.HOME_SCREEN)
            }
        }
        else -> {

        }
    }

    when(googleState){
        is ResourceState.Error -> {
            (googleState as ResourceState.Error).errorBody?.let { ShowMessage(it.message) { loginViewModel.resetGoogleState() } }
        }
        else -> {

        }
    }


    Scaffold(
        content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(id = R.dimen.padding_app))
                        .verticalScroll(rememberScrollState())
                        .background(color = MaterialTheme.colorScheme.background)

                ) {
                    when(checkLogin){
                        is ResourceState.Loading -> {
                            Loading()
                        }
                        is ResourceState.Error -> {
                            ShowMessage(
                                message = "Phiên đăng nhập hết hạn",
                                onReset = {
                                    loginViewModel.resetState()
                                }
                            )
                        }
                        is ResourceState.Nothing -> {
                            HeaderApp(
                                painterResource(id = R.drawable.choose),
                                stringResource(id = R.string.app_name),
                                stringResource(id = R.string.login)
                            )
                            TextFieldOutlined(
                                loginViewModel.userName,
                                onChangeValue = {
                                    loginViewModel.onChangeUserName(it)
                                },
                                stringResource(id = R.string.user_name),
                                painterResource(id = R.drawable.person)
                            )
                            Spacer(
                                modifier = Modifier
                                    .height(dimensionResource(id = R.dimen.space_app_small))
                            )
                            PasswordFieldOutlined(
                                loginViewModel.password,
                                onChangeValue = {
                                    loginViewModel.onChangePassword(it)
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
                                    loginViewModel.login()
                                },
                                stringResource(id = R.string.login),
                                MaterialTheme.colorScheme.primary,
                                auth is ResourceState.Loading,
                                auth !is ResourceState.Loading
                            )

                            Spacer(
                                modifier = Modifier
                                    .height(dimensionResource(id = R.dimen.space_app_normal))
                            )

                            ButtonComponent(
                                onClick = {
                                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestEmail()
                                        .requestIdToken("394048496036-grmhp72aso4vobp78l4cvo950ot9b44a.apps.googleusercontent.com")
                                        .build()

                                    val googleSingInClient = GoogleSignIn.getClient(context, gso)

                                    launcher.launch(googleSingInClient.signInIntent)
                                },
                                "Đăng Nhập Bằng Google",
                                MaterialTheme.colorScheme.primary,
                                authGoogle is ResourceState.Loading,
                                authGoogle !is ResourceState.Loading
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

                            Spacer(
                                modifier = Modifier
                                    .height(dimensionResource(id = R.dimen.space_app_normal))
                            )

                            SmallText(
                                stringResource(id = R.string.to_verify_email),
                                TextAlign.Start,
                                MaterialTheme.colorScheme.onBackground,
                                navController,
                                Routes.SEND_EMAIL_VERIFY_SCREEN)

                            Spacer(
                                modifier = Modifier
                                    .height(dimensionResource(id = R.dimen.space_app_normal))
                            )

                            SmallText(
                                stringResource(id = R.string.forgot_password),
                                TextAlign.Start,
                                MaterialTheme.colorScheme.onBackground,
                                navController,
                                Routes.SEND_EMAIL_FORGOT_SCREEN)

                            Spacer(
                                modifier = Modifier
                                    .height(dimensionResource(id = R.dimen.space_app_normal))
                            )
                        }
                        else -> {

                        }
                    }
                }
        }
    )
}

@Composable
private fun ShowMessage(
    message: String,
    onReset: () -> Unit = {}
) {
    Toast.makeText(
        LocalContext.current,
        message,
        Toast.LENGTH_LONG
    ).show()
    onReset()
}

