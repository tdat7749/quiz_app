package com.example.client.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.*
import com.example.client.R
import com.example.client.model.HistoryAnswer
import com.example.client.model.HistoryRank
import com.example.client.model.Room
import com.example.client.model.User
import com.example.client.ui.components.ButtonNavigate
import com.example.client.ui.components.Loading
import com.example.client.ui.components.LoadingCircle
import com.example.client.ui.components.TopBar
import com.example.client.ui.components.quiz.LandingImage
import com.example.client.ui.navigation.Routes
import com.example.client.ui.viewmodel.WaitingRoomViewModel
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState


@Composable
fun WaitingRoomScreen(
    roomPin:String,
    roomId:Int,
    navController: NavController,
    waitingRoomViewModel: WaitingRoomViewModel = hiltViewModel()
){

    var selectedTabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Xếp Hạng", "Đáp Án Của Bạn","Người Chơi")

    val history = waitingRoomViewModel.getHistoryRankRoom(roomId).collectAsLazyPagingItems()
    val users = waitingRoomViewModel.getUsersInRoom(roomId).collectAsLazyPagingItems()
    val room by waitingRoomViewModel.join.collectAsState()
    val answers by waitingRoomViewModel.answer.collectAsState()
    val kick by waitingRoomViewModel.kick.collectAsState()

    when(kick){
        is ResourceState.Success -> {
            ShowMessage(
                message = (kick as ResourceState.Success<ApiResponse<Boolean>>).value.message,
                onReset = {
                    waitingRoomViewModel.resetKickState()
                }
            )
            users.refresh()
        }
        is ResourceState.Error -> {
            (kick as ResourceState.Error).errorBody?.let {
                ShowMessage(
                    message = it.message,
                    onReset = {
                        waitingRoomViewModel.resetKickState()
                    }
                )
            }
        }
        else -> {

        }
    }

    LaunchedEffect(key1 = roomPin,key2 = roomId){
        waitingRoomViewModel.getRoomForParticipants(roomPin)
    }

    LaunchedEffect(key1 = roomId){
        waitingRoomViewModel.getHistoryAnswer(roomId)
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Phòng Chờ",
                navController = navController
            )
        },
        bottomBar = {
            if(room is ResourceState.Success && answers is ResourceState.Success){
                val roomm = (room as ResourceState.Success<ApiResponse<Room>>).value.data
                val answerss = (answers as ResourceState.Success<ApiResponse<List<HistoryAnswer>>>).value.data
                if(roomm.playAgain || answerss.size == 0){
                    ButtonNavigate(
                        value = "Bắt Đầu",
                        onClick = {
                            navController.navigate("${Routes.PLAY_QUIZ_SCREEN}/${roomm.quiz.id}/${roomm.id}")
                        },
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.padding_app))
                    .background(color = MaterialTheme.colorScheme.background)
//                    .verticalScroll(rememberScrollState())
                    .padding(it)
            ){
                if(room is ResourceState.Loading || answers is ResourceState.Loading || history.loadState.refresh is LoadState.Loading){
                    Loading()
                }else if (room is ResourceState.Success && answers is ResourceState.Success && history.loadState.refresh is LoadState.NotLoading){
                    val roomDetail = (room as ResourceState.Success<ApiResponse<Room>>).value.data
                    val listAnswers = (answers as ResourceState.Success<ApiResponse<List<HistoryAnswer>>>).value.data
                    WaitingImage(roomDetail.quiz.thumbnail)

                    Spacer(
                        modifier = Modifier.height(dimensionResource(id = R.dimen.space_app_normal))
                    )

                    CardHeading(roomDetail.quiz.title,roomDetail.totalUser,roomDetail.maxUser)

                    TabRow(selectedTabIndex) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                text = { Text(title) }
                            )
                        }
                    }

                    when (selectedTabIndex) {
                        0 -> RankContent(history)
                        1 -> AnswerContent(listAnswers)
                        2 -> UsersContent(
                            users = users,
                            isHost = roomDetail.onwer ?: false,
                            roomId = roomId,
                            viewModel = waitingRoomViewModel
                        )
                    }
                }
            }
        }
    )
}


@Composable
fun RankContent(history: LazyPagingItems<HistoryRank>){
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(history.itemCount){ index ->
            val item = history[index]
            if (item != null) {
                RankItem(
                    history = item,
                    index = index
                )
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
fun UsersContent(users: LazyPagingItems<User>,viewModel: WaitingRoomViewModel,isHost:Boolean,roomId: Int){
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(users.itemCount){ index ->
            val item = users[index]
            if (item != null) {
                UserItem(
                    user = item,
                    index = index,
                    viewModel = viewModel,
                    isHost = isHost,
                    roomId = roomId
                )
            }
        }
        item{
            if(users.loadState.append is LoadState.Loading){
                LoadingCircle()
            }
        }
    }
}

@Composable
fun UserItem(index:Int,user:User,viewModel: WaitingRoomViewModel,isHost: Boolean,roomId: Int){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.crowns))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Row (
            modifier =  Modifier.fillMaxSize()
                .padding(start = 10.dp,end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                if(index == 0){
                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier
                            .size(80.dp)
                            .padding(bottom = 1.dp)
                    )
                }else{
                    Text(
                        modifier = Modifier
                            .padding(start = 35.dp, end = 35.dp),
                        text = "${index + 1}",
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = user.displayName
                )
            }
            if(isHost){
                IconButton(
                    onClick = {
                        viewModel.kickUser(roomId,user.id)
                    }
                ){
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Composable
fun RankItem(index:Int,history:HistoryRank){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.crowns))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Row (
            modifier =  Modifier.fillMaxSize()
                .padding(start = 10.dp,end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                if(index == 0){
                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier
                            .size(80.dp)
                            .padding(bottom = 1.dp)
                    )
                }else{
                    Text(
                        modifier = Modifier
                            .padding(start = 35.dp, end = 35.dp),
                        text = "${index + 1}",
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = history.user.displayName
                )
            }
            Text(
                text = "${history.totalScore}",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Composable
fun CardHeading(title:String,totalUser:Int,maxUser:Int){
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.category_card_height)),
        shape = MaterialTheme.shapes.medium
    ) {
        Column (
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .height(90.dp)
                    .fillMaxSize()
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = title,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .height(30.dp)
            ){
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .align(Alignment.Center),
                    horizontalArrangement = Arrangement.Center
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.group),
                        contentDescription = "group icon"
                    )
                    Text(
                        text= "${totalUser}/${maxUser}",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun WaitingImage(thumbnail:String?) {
    AsyncImage(
        model = thumbnail,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.category_card_width))
            .shadow(4.dp,shape = RoundedCornerShape(8.dp))
    )
}



@Composable
private fun Answerd(index:Int,history:HistoryAnswer){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "${index + 1} - ${history.question.title}",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row (
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(
                    model = history.question.thumbnail,
                    contentDescription = null,
                    modifier = Modifier
                        .height(55.dp)
                        .width(70.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = history.question.title,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if(history.correct) Color.Green else MaterialTheme.colorScheme.primary
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 6.dp, end = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,

                ) {
                    Text(
                        text=history.answer?.title ?: "Không Chọn Đáp Án",
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.background
                    )
                    Icon(
                        painter = if(history.correct) painterResource(id = R.drawable.done) else painterResource(id = R.drawable.close),
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.background,
                    )
                }
            }
        }
    }
}

@Composable
fun AnswerContent(history: List<HistoryAnswer>){
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(history){index, item ->
            Answerd(index,item)
        }
    }
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