package com.example.client.ui.screens

import android.annotation.SuppressLint
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultQuizScreen(
    navController: NavController,
    playQuizViewModel: PlayQuizViewModel
){

    DisposableEffect(Unit){
        onDispose {
            playQuizViewModel.resetState()
        }
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
                        //chuyeeren hướng sang trang rank
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
                        navController.navigate(Routes.HOME_SCREEN)
                    },
                    "Thoát",
                    MaterialTheme.colorScheme.secondary
                )
            }
        }
    )
}