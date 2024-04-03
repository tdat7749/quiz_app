package com.example.client.ui.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.client.R
import com.example.client.ui.components.ButtonNavigate
import com.example.client.ui.components.HeaderApp
import com.example.client.ui.navigation.Routes
import com.example.client.ui.viewmodel.PlayQuizViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ResultQuizScreen(
    quizId:Int,
    roomId:Int?,
    navController: NavController,
    playQuizViewModel: PlayQuizViewModel
){

    BackHandler(enabled = true){
        // không cho người dùng quay lại bài làm
    }

    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.padding_app))
                    .verticalScroll(rememberScrollState())
            ){
                HeaderApp(
                    painterResource = painterResource(id = R.drawable.quiz),
                    headingText = "KẾT QUẢ",
                    normalText = ""
                )

                Spacer(
                    modifier = Modifier
                        .height(dimensionResource(id = R.dimen.space_app_small))
                )

                Text(
                    text = "Đã Trả Lời: ${playQuizViewModel.totalAnswered}",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold
                )


                Spacer(
                    modifier = Modifier
                        .height(dimensionResource(id = R.dimen.space_app_small))
                )

                Text(
                    text = "Số Câu Đúng ${playQuizViewModel.totalCorrect}",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier
                        .height(dimensionResource(id = R.dimen.space_app_small))
                )

                Text(
                    text = "Tổng Điểm: ${playQuizViewModel.totalScore}",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier
                        .height(dimensionResource(id = R.dimen.space_app_extraLarge))
                )

                ButtonNavigate(
                    onClick = {
                        navController.navigate("${Routes.QUIZ_RANK_SCREEN}/${quizId}/${roomId.toString()}")
                    },
                    "Xem Xếp Hạng",
                    MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier
                        .height(dimensionResource(id = R.dimen.space_app_small))
                )

                ButtonNavigate(
                    onClick = {
                        playQuizViewModel.resetState()
                        navController.navigate(Routes.HOME_SCREEN)
                    },
                    "Thoát",
                    MaterialTheme.colorScheme.secondary
                )
            }
        }
    )
}