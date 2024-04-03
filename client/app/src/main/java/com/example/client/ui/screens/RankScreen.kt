package com.example.client.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.*
import com.example.client.model.HistoryRank
import com.example.client.model.User
import com.example.client.ui.components.Loading
import com.example.client.ui.components.LoadingCircle
import com.example.client.ui.components.TopBar
import com.example.client.ui.viewmodel.RankScreenViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RankScreen(
    quizId:Int,
    roomId:Int?,
    navController:NavController,
    rankScreenViewModel: RankScreenViewModel = hiltViewModel()
){

    var historyRank: LazyPagingItems<HistoryRank>? = null;
    if(roomId != null){
        historyRank = rankScreenViewModel.getHistoryRankRoom(roomId).collectAsLazyPagingItems()
    }else{
        historyRank = rankScreenViewModel.getHistoryRankSingle(quizId).collectAsLazyPagingItems()
    }

    val defaultRankUser = HistoryRank(
        user = User(
            displayName = "CHƯA CÓ",
            avatar = "",
            id = -1
        ),
        totalCorrect = 0,
        totalScore = 0
    )

    Scaffold(
        topBar = {
            TopBar("Xếp Hạng",navController)
        },
        content = {
            Surface (
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .verticalScroll(rememberScrollState())
                    .padding(it)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = 32.dp,
                            start = 16.dp,
                            end = 16.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    if(historyRank.loadState.refresh is LoadState.Loading){
                        Loading()
                    }else if (historyRank.loadState.refresh is LoadState.NotLoading){
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            TopThreeSection(
                                first = if (historyRank.itemCount >= 1) historyRank[0] else defaultRankUser,
                                second = if (historyRank.itemCount >= 2) historyRank[1] else defaultRankUser,
                                third = if (historyRank.itemCount >= 3) historyRank[2] else defaultRankUser
                            )
                        }
                        QueueSection(
                            modifier = Modifier.weight(1f),
                            history = historyRank //.slice(3 until historyRank.itemCount)
                        )
                    }
                }
            }
        }
    )
}



@Composable
private fun TopThreeSection(
    first: HistoryRank?,
    second: HistoryRank?,
    third: HistoryRank?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        second?.user?.let {
            Second(
                userImage = it.avatar,
                userName = second.user.displayName,
                userScore = second.totalScore
            )
        }
        first?.user?.let {
            First(
                userImage = it.avatar,
                userName = first.user.displayName,
                userScore = first.totalScore
            )
        }
        third?.user?.let {
            Third(
                userImage = it.avatar,
                userName = third.user.displayName,
                userScore = third.totalScore
            )
        }
    }
}

// Created for TopThree
@Composable
private fun First(userImage: String, userScore: Int, userName: String) {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(com.example.client.R.raw.crowns))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    Column(
        modifier = Modifier
            .padding(bottom = 24.dp)
            .zIndex(2f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(bottom = 2.dp),
            text = "1",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .size(80.dp)
                .padding(bottom = 1.dp)
        )
        ProfileImage(
            userImage = userImage,
            imageSize = 136.dp
        )
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = userName,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = "$userScore",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

// Created for TopThree
@Composable
private fun Second(userImage: String, userName: String, userScore: Int) {
    Column(
        modifier = Modifier
            .offset(x = 32.dp)
            .zIndex(1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = "2",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        ProfileImage(
            userImage = userImage
        )
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = userName,
            style = MaterialTheme.typography.titleMedium,
//            color = MaterialTheme.colors.primaryVariant
        )
        Text(
            text = "$userScore",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
//            color = MaterialTheme.colors.primaryVariant
        )
    }
}

// Created for TopThree
@Composable
private fun Third(userImage: String, userName: String, userScore: Int) {
    Column(
        modifier = Modifier
            .offset(x = (-32).dp)
            .zIndex(1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = "3",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        ProfileImage(userImage = userImage)
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = userName,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = "$userScore",
            fontWeight = FontWeight.Bold
//            color = MaterialTheme.colors.primaryVariant
        )
    }
}

@Composable
private fun ProfileImage(userImage: String, imageSize: Dp = 112.dp) {
    Card(
        modifier = Modifier
            .size(imageSize)
            .shadow(
                ambientColor = MaterialTheme.colorScheme.primary,
                spotColor = MaterialTheme.colorScheme.primary,
                elevation = 32.dp,
                shape = CircleShape
            ),
        shape = CircleShape,
        border = BorderStroke(
            width = 4.dp,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.secondary,
                    MaterialTheme.colorScheme.tertiary,
                    MaterialTheme.colorScheme.onPrimary,
                    MaterialTheme.colorScheme.onSecondary,
                    MaterialTheme.colorScheme.onTertiary
                )
            )
        ),
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            model = userImage,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun QueueSection(modifier: Modifier, history: LazyPagingItems<HistoryRank>) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(history.itemCount){ index ->
            if(index > 2){
                val item = history[index]
                if (item != null) {
                    Queue(
                        userImage = item.user.avatar,
                        userName = item.user.displayName,
                        userScore = item.totalScore,
                        position = index + 1
                    )
                }
            }
        }
        item{
            if(history.loadState.append is LoadState.Loading){
                LoadingCircle()
            }
        }
    }
}

@Composable
private fun Queue(
    userImage: String,
    userName: String,
    userScore: Int,
    position: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$position",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Row(
            modifier = Modifier
                .padding(start = 32.dp)
                .height(80.dp)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(50)
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            QueueContent(
                userImage = userImage,
                userName = userName,
                userScore = userScore
            )
        }
    }
}

// Created for Queue
@Composable
private fun QueueContent(userImage: String, userName: String, userScore: Int) {
    Row(
        modifier = Modifier.fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(80.dp)
                .clip(shape = CircleShape),
            model = userImage,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = userName,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
    Text(
        modifier = Modifier.padding(end = 32.dp),
        text = "$userScore",
        color = MaterialTheme.colorScheme.onPrimary,
        fontWeight = FontWeight.Bold
    )
}