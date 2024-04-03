package com.example.client.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.imageLoader
import com.example.client.R
import com.example.client.model.QuizDetail
import com.example.client.ui.components.ButtonComponent
import com.example.client.ui.components.ButtonNavigate
import com.example.client.ui.components.Loading
import com.example.client.ui.components.TopBar
import com.example.client.ui.navigation.Routes
import com.example.client.ui.viewmodel.QuizLandingViewModel
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState


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
            Surface (
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .verticalScroll(rememberScrollState())
                    .padding(it)
            ){
                when(quiz){
                    is ResourceState.Loading -> {
                        Loading()
                    }
                    is ResourceState.Success -> {
                        val quizDetail = (quiz as ResourceState.Success<ApiResponse<QuizDetail>>).value.data

                       Column (
                           modifier = Modifier
                               .fillMaxSize()
                               .padding(dimensionResource(id = R.dimen.padding_app))
                       ) {
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

                           if(quizDetail.collect){
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
private fun LandingImage(thumbnail:String) {
    AsyncImage(
        model = thumbnail,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.quiz_card_height))
            .shadow(4.dp,shape = RoundedCornerShape(8.dp))
    )
}

@Composable
private fun QuizName(quizName: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 64.dp),
        text = quizName,
        fontSize = 32.sp,
        textAlign = TextAlign.Start,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun QuizAuthorNameAndCreatedDate(
    name: String,
    createdDate: String,
    avatar: String?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            model = avatar,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = name,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "-",
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Icon(
            painter = painterResource(id = R.drawable.calendar),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = createdDate,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun QuizCategoryName(
    categoryName: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.category),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = categoryName,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 24.sp
        )
    }
}

@Composable
private fun QuizDescription(title:String,quizDescription: String) {
    Row (
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = "$title $quizDescription",
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.secondary
        )
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