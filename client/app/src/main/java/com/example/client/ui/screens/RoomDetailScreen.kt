package com.example.client.ui.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.client.R
import com.example.client.model.Room
import com.example.client.ui.components.ButtonNavigate
import com.example.client.ui.components.Loading
import com.example.client.ui.components.TopBar
import com.example.client.ui.components.quiz.LandingImage
import com.example.client.ui.components.room.*
import com.example.client.ui.components.room.TimeRoom
import com.example.client.ui.navigation.Routes
import com.example.client.ui.viewmodel.RoomDetailViewModel
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import com.example.client.utils.Utilities


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RoomDetailScreen(
    id:Int,
    navController: NavController,
    roomDetailViewModel: RoomDetailViewModel = hiltViewModel()
){

    val room by roomDetailViewModel.room.collectAsState()

    LaunchedEffect(key1 = id){
        roomDetailViewModel.getRoomDetail(id)
    }

    Scaffold(
        topBar = {
            TopBar(
                "Chi Tiết Phòng Chơi",
                navController
            )
        },
        content = {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.padding_app))
                    .background(color = MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
                    .padding(it)
            ) {
                when(room){
                    is ResourceState.Loading -> {
                        Loading()
                    }
                    is ResourceState.Success -> {
                        val room = (room as ResourceState.Success<ApiResponse<Room>>).value.data

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                LandingImage(
                                    room.quiz.thumbnail
                                )
                                Spacer(
                                    modifier = Modifier
                                        .height(dimensionResource(id = R.dimen.space_app_normal))
                                )
                                RoomName(
                                    name = room.roomName
                                )
                                RoomPin(
                                    pin = room.roomPin
                                )
                                PlayAgain(
                                    isPlayAgain = room.playAgain
                                )
                                UserNumber(
                                    totalUser = room.totalUser,
                                    maxUser = room.maxUser
                                )
                                TimeRoom(
                                    timeStart = Utilities.Companion.formatDateInvert(if(room.timeStart == null) null else room.timeStart.toString()),
                                    timeEnd = Utilities.Companion.formatDateInvert(if(room.timeEnd == null) null else room.timeEnd.toString())
                                )
                                RoomStatus(
                                    title = "Trạng Thái",
                                    status = room.closed
                                )

                                Spacer(
                                    modifier = Modifier
                                        .height(dimensionResource(id = R.dimen.space_app_large))
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                ButtonNavigate(
                                    onClick = {
                                        navController.navigate("${Routes.WAITING_ROOM_SCREEN}/${room.roomPin}/${room.id}")
                                    },
                                    "Phòng Chờ",
                                    MaterialTheme.colorScheme.primary
                                )

                                Spacer(
                                    modifier = Modifier
                                        .height(dimensionResource(id = R.dimen.space_app_normal))
                                )

                                ButtonNavigate(
                                    onClick = {
                                        navController.navigate("${Routes.EDIT_ROOM_SCREEN}/${id}")
                                    },
                                    "Cập Nhật",
                                    MaterialTheme.colorScheme.secondary
                                )
                            }

                        }
                    is ResourceState.Error ->{
                        (room as ResourceState.Error).errorBody?.message?.let { it1 -> ShowMessage(it1) }
                        LaunchedEffect(Unit){
                            navController.navigate(Routes.HOME_SCREEN)
                        }
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
) {
    Toast.makeText(
        LocalContext.current,
        message,
        Toast.LENGTH_LONG
    ).show()
}