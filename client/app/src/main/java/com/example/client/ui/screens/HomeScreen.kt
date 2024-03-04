package com.example.client.ui.screens

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
import com.example.client.ui.components.HeadingBoldText
import com.example.client.ui.components.Loading
import com.example.client.ui.components.QuizCard
import com.example.client.ui.navigation.Routes
import com.example.client.ui.theme.Shapes
import com.example.client.ui.viewmodel.HomeViewModel
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import com.example.client.utils.SharedPreferencesManager

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
){
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(dimensionResource(id = R.dimen.padding_app)),
        color = Color.White
    ) {
        val topics by homeViewModel.topics.collectAsState()

        LaunchedEffect(Unit){
            homeViewModel.getAllTopic()
            if(SharedPreferencesManager.getUser(User::class.java) == null){
                homeViewModel.getMe()
            }
        }
        Column {
            when(topics){
                is ResourceState.Loading -> {
                    Loading()
                }
                is ResourceState.Success-> {
                    UserInfo(homeViewModel.getMe())
                    SectionTopic(
                        "Chủ Đề",
                        (topics as ResourceState.Success<ApiResponse<List<Topic>>>).value.data,
                        navController)
                }
                is ResourceState.Error -> {
                    ShowMessage("Có lỗi xảy ra")
                }
                else -> {

                }
            }
        }
    }
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
                    model = "https://www.proprofs.com/quiz-school/topic_images/p191f89lnh17hs1qnk9fj1sm113b3.jpg",
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
                .height(dimensionResource(id = R.dimen.avatar))
                .width(dimensionResource(id = R.dimen.avatar))
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
fun SectionQuiz(title:String,items: List<Quiz>){
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
                QuizCard(item)
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