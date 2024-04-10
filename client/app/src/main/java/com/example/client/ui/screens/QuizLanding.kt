package com.example.client.ui.screens

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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
import com.example.client.model.QuizDetail
import com.example.client.ui.components.ButtonComponent
import com.example.client.ui.components.ButtonNavigate
import com.example.client.ui.components.Loading
import com.example.client.ui.components.TopBar
import com.example.client.ui.components.quiz.*
import com.example.client.ui.navigation.Routes
import com.example.client.ui.viewmodel.QuizLandingViewModel
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import com.example.client.utils.Utilities

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizLanding(
    id:Int,
    quizLandingViewModel: QuizLandingViewModel = hiltViewModel(),
    navController:NavController
) {

    val quiz by quizLandingViewModel.quiz.collectAsState()
    val collect by quizLandingViewModel.collect.collectAsState()

    LaunchedEffect(key1 = id) {
        quizLandingViewModel.getQuizDetail(id)
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
                    .background(color = MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
                    .padding(it)
            ) {
                when(quiz){
                    is ResourceState.Loading -> {
                        Loading()
                    }
                    is ResourceState.Success -> {
                        val quizDetail = (quiz as ResourceState.Success<ApiResponse<QuizDetail>>).value.data


                           LandingImage(quizDetail.thumbnail)
                           QuizName(
                               quizName = quizDetail.title
                           )
                           QuizAuthorNameAndCreatedDate(
                               name = quizDetail.user.displayName,
                               createdDate = quizDetail.createdAt,
                               avatar = quizDetail.user.avatar
                           )
                           QuizCategoryName(
                               categoryName = quizDetail.topic.title
                           )
                           QuizDescription(
                               title = "Tóm Tắt: ",
                               quizDescription = quizDetail.summary
                           )

                           Spacer(
                               modifier = Modifier
                                   .height(dimensionResource(id = R.dimen.space_app_large))
                           )

                           QuizDescription(
                               title = "Chi Tiết: ",
                               quizDescription = quizDetail.description
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
                                   navController.navigate("${Routes.QUIZ_RANK_SCREEN}/${id}/null")
                               },
                               "Bảng Xếp Hạng",
                               MaterialTheme.colorScheme.primary
                           )

                           Spacer(
                               modifier = Modifier
                                   .height(dimensionResource(id = R.dimen.space_app_normal))
                           )


                           if(quizDetail.collect || quizDetail.owner){
                               ButtonNavigate(
                                   onClick = {
                                        navController.navigate("${Routes.CREATE_ROOM_SCREEN}/${id}/${Uri.encode(quizDetail.title)}/${Uri.encode(quizDetail.thumbnail)}")
                                   },
                                   "Tạo Phòng",
                                   MaterialTheme.colorScheme.secondary
                               )
                           }else{
                               ButtonComponent(
                                   onClick = {
                                        quizLandingViewModel.collectQuiz(quizDetail)
                                   },
                                   value = "Thêm Vào Bộ Sưu Tập",
                                   color = MaterialTheme.colorScheme.secondary,
                                   loading = collect is ResourceState.Loading,
                                   enable =  quiz !is ResourceState.Loading
                               )
                           }
                       }
                    is ResourceState.Error ->{
                        (quiz as ResourceState.Error).errorBody?.message?.let { it1 -> ShowMessage(it1) }
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