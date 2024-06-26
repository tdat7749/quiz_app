package com.example.client.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.example.client.ui.components.TopBar
import com.example.client.ui.viewmodel.FindRoomViewModel
import com.example.client.R
import com.example.client.model.Room
import com.example.client.ui.components.ButtonComponent
import com.example.client.ui.components.TextFieldOutlined
import com.example.client.ui.navigation.Routes
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FindRoomScreen(
    navController: NavController,
    findRoomViewModel: FindRoomViewModel = hiltViewModel()
){

    

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.join_room))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    val join by findRoomViewModel.join.collectAsState()

    DisposableEffect(Unit){
            onDispose {
                findRoomViewModel.resetState()
            }
        }


    when(join){
        is ResourceState.Success -> {
            val roomId = (join as ResourceState.Success<ApiResponse<Int>>).value.data
            LaunchedEffect(Unit){
                navController.navigate("${Routes.WAITING_ROOM_SCREEN}/${findRoomViewModel.roomPin}/${roomId}")
            }
        }
        is ResourceState.Error -> {
            (join as ResourceState.Error).errorBody?.let { ShowMessage(it.message) { findRoomViewModel.resetState() } }
        }
        else -> {

        }
    }

   
    Scaffold(
        topBar = {
            TopBar(
                "Tham Gia Phòng Chơi",
                navController
            )
        },
        content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(id = R.dimen.padding_app))
                        .background(color = MaterialTheme.colorScheme.background)
                        .verticalScroll(rememberScrollState())
                        .padding(it)
                ) {
                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                    )
                    Spacer(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.space_app_normal))
                    )
                    TextFieldOutlined(
                        findRoomViewModel.roomPin,
                        onChangeValue = {value ->
                            findRoomViewModel.onChangeRoomPin(value)
                        },
                        "Mã PIN",
                        painterResource(id = R.drawable.password)
                    )

                    Spacer(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.space_app_small))
                    )
                    ButtonComponent(
                        value = "Tham Gia",
                        onClick = {
                            findRoomViewModel.joinRoom()
                        },
                        color = MaterialTheme.colorScheme.primary,
                        loading = join is ResourceState.Loading,
                        enable = join !is ResourceState.Loading
                    )
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