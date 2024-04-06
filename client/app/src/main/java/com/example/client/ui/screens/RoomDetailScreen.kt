package com.example.client.ui.screens

import android.widget.Toast
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
import com.example.client.ui.components.room.RoomName
import com.example.client.ui.components.room.RoomStatus
import com.example.client.ui.components.room.TimeRoom
import com.example.client.ui.navigation.Routes
import com.example.client.ui.viewmodel.RoomDetailViewModel
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState


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
                "Chi Tiết Quiz",
                navController
            )
        },
        content = {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.padding_app))
                    .background(Color.White)
                    .verticalScroll(rememberScrollState())
                    .padding(it)
            ) {
                when(room){
                    is ResourceState.Loading -> {
                        Loading()
                    }
                    is ResourceState.Success -> {
                        val room = (room as ResourceState.Success<ApiResponse<Room>>).value.data

                            RoomName(
                                name = room.roomName
                            )
                            TimeRoom(
                                timeStart = room.timeStart,
                                timeEnd = room.timeEnd
                            )
                            RoomStatus(
                                title = "Trạng Thái",
                                status = room.closed
                            )

                            Spacer(
                                modifier = Modifier
                                    .height(dimensionResource(id = R.dimen.space_app_large))
                            )

                            ButtonNavigate(
                                onClick = {
                                    navController.navigate("${Routes.PLAY_QUIZ_SCREEN}/${id}/null")
                                },
                                "Bắt Đầu Chơi",
                                MaterialTheme.colorScheme.primary
                            )

                            Spacer(
                                modifier = Modifier
                                    .height(dimensionResource(id = R.dimen.space_app_normal))
                            )

                            ButtonNavigate(
                                onClick = {
                                    navController.navigate("${Routes.QUIZ_RANK_SCREEN}/${id}/${room.id}")
                                },
                                "Bảng Xếp Hạng",
                                MaterialTheme.colorScheme.primary
                            )

                            Spacer(
                                modifier = Modifier
                                    .height(dimensionResource(id = R.dimen.space_app_normal))
                            )

                            ButtonNavigate(
                                onClick = {
//                                    navController.navigate("${Routes.CREATE_ROOM_SCREEN}/${id}/${Uri.encode(quizDetail.title)}/${Uri.encode(quizDetail.thumbnail)}")
                                },
                                "Cập Nhật",
                                MaterialTheme.colorScheme.secondary
                            )

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