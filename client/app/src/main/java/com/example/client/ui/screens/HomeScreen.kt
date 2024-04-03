package com.example.client.ui.screens

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.client.R
import com.example.client.model.Quiz
import com.example.client.model.Topic
import com.example.client.model.User
import com.example.client.ui.components.*
import com.example.client.ui.navigation.Routes
import com.example.client.ui.theme.Shapes
import com.example.client.ui.viewmodel.HomeViewModel
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import com.example.client.utils.SharedPreferencesManager

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
){
    val topics by homeViewModel.topics.collectAsState()
    val quizTop10 by homeViewModel.quizTop10.collectAsState()
    val user by homeViewModel.user.collectAsState()
    val quizLatest by homeViewModel.quizLatest.collectAsState()

    Scaffold(
        bottomBar =  {
            BottomBar(navController)
        },
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                color = MaterialTheme.colorScheme.background
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top
                ) {
                    ScreenHeader("", painterResource = painterResource(id = R.drawable.quiz_time), navController = navController)
                    Spacer(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.space_app_large))
                    )
                    Column (modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_app))) {
                        when{
                            topics is ResourceState.Loading ||
                                    user is ResourceState.Loading ||
                                    quizLatest is ResourceState.Loading ||
                                    quizTop10 is ResourceState.Loading -> {
                                Loading()
                            }
                            topics is ResourceState.Success &&
                                    user is ResourceState.Success &&
                                    quizLatest is ResourceState.Success &&
                                    quizTop10 is ResourceState.Success-> {
//                        UserHeader((user as ResourceState.Success<ApiResponse<User>>).value.data)
//                        Spacer(
//                            modifier = Modifier
//                                .height(dimensionResource(id = R.dimen.space_app_extraLarge))
//                        )
                                SectionTopic(
                                    "Chủ Đề",
                                    (topics as ResourceState.Success<ApiResponse<List<Topic>>>).value.data,
                                    navController)
                                Spacer(
                                    modifier = Modifier
                                        .height(dimensionResource(id = R.dimen.space_app_extraLarge))
                                )
                                SectionQuiz(
                                    "Mới Nhất",
                                    (quizLatest as ResourceState.Success<ApiResponse<List<Quiz>>>).value.data,
                                    navController
                                )
                                Spacer(
                                    modifier = Modifier
                                        .height(dimensionResource(id = R.dimen.space_app_extraLarge))
                                )
                                SectionQuiz(
                                    "Được Yêu Thích Nhất",
                                    (quizTop10 as ResourceState.Success<ApiResponse<List<Quiz>>>).value.data,
                                    navController
                                )
                                Spacer(
                                    modifier = Modifier
                                        .height(dimensionResource(id = R.dimen.space_app_extraLarge))
                                )
                            }
                            user is ResourceState.Error -> {
                                (user as ResourceState.Error).errorBody?.let { ShowMessage(it.message) }
                            }
                            topics is ResourceState.Error -> {
                                (topics as ResourceState.Error).errorBody?.let { ShowMessage(it.message) }
                            }
                            quizLatest is ResourceState.Error -> {
                                (quizLatest as ResourceState.Error).errorBody?.let { ShowMessage(it.message) }
                            }
                            quizTop10 is ResourceState.Error -> {
                                (quizTop10 as ResourceState.Error).errorBody?.let { ShowMessage(it.message) }
                            }
                            else -> {

                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun TopicCard(topic: Topic,navController:NavController){
    Box (
        modifier = Modifier
            .clickable {
                navController.navigate("${Routes.TOPIC_SCREEN}/${topic.id}/${Uri.encode(topic.title)}/${Uri.encode(topic.thumbnail)}")
            }
    ){
        Card(
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.category_card_height))
                .width(dimensionResource(id = R.dimen.category_card_width))
        ){
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                AsyncImage(
                    model = topic.thumbnail,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.7f),
                    contentScale = ContentScale.Crop
                )
                HeadingBoldText(
                    topic.title,
                    TextAlign.Center,
                    MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp),
                    fontSize = 24.sp
                )
            }
        }
    }
}

@Composable
fun QuizCardScroll(quiz: Quiz){
    Box(){
        Card(
            modifier = Modifier
                .width(dimensionResource(id = R.dimen.quiz_card_width))
                .height(dimensionResource(id = R.dimen.quiz_card_height))
                .shadow(4.dp,shape = RoundedCornerShape(8.dp)),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary)
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                AsyncImage(
                    model = quiz.thumbnail,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = R.dimen.image_height))
                )
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 8.dp, bottom = 8.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    HeadingBoldText(
                        quiz.id.toString(),
                        TextAlign.Start,
                        MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth(),
                        fontSize = 24.sp
                    )
                    UserInfo(quiz.user)
                }
            }
        }
    }
}



@Composable
fun UserInfo(user:User){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        AsyncImage(
            model = "https://www.proprofs.com/quiz-school/topic_images/p191f89lnh17hs1qnk9fj1sm113b3.jpg",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.avatar_quiz))
                .width(dimensionResource(id = R.dimen.avatar_quiz))
                .clip(Shapes.extraSmall),
        )

        Text(
            text = user.displayName,
            modifier = Modifier
                .padding(start = 8.dp),
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Composable
fun UserHeader(user:User){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(100.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        AsyncImage(
            model = "https://www.proprofs.com/quiz-school/topic_images/p191f89lnh17hs1qnk9fj1sm113b3.jpg",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.avatar))
                .width(dimensionResource(id = R.dimen.avatar))
                .clip(Shapes.extraSmall),
        )

        Text(
            text = user.displayName,
            modifier = Modifier
                .padding(start = 8.dp),
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Composable
fun SectionQuiz(title:String,items: List<Quiz>,navController: NavController){
    Column (
        modifier = Modifier
            .fillMaxWidth()
    ){
        Text(
            text = title,
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.space_app_normal))
        )
        LazyRow (
            horizontalArrangement  = Arrangement.spacedBy(8.dp)
        ) {
            items(items){ item ->
                QuizCard(item, navController)
            }
        }
    }
}

@Composable
fun SectionTopic(title:String,items: List<Topic>,navController: NavController){
    Column (
        modifier = Modifier
            .fillMaxWidth()
    ){
        Text(
            text = title,
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.space_app_normal))
        )
        LazyRow (
            horizontalArrangement  = Arrangement.spacedBy(8.dp)
        ) {
            items(items){ item ->
                TopicCard(item,navController)
            }
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