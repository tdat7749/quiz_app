package com.example.client.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.*
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.*
import com.example.client.R
import com.example.client.model.HistoryAnswer
import com.example.client.model.User
import com.example.client.ui.components.ButtonNavigate
import com.example.client.ui.components.Loading
import com.example.client.ui.components.TopBar
import com.example.client.ui.viewmodel.RealTimeViewModel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun WaitingRoomRealTimeScreen(
    roomPin:String,
    roomId:Int,
    navController: NavController,
    realTimeViewModel: RealTimeViewModel = hiltViewModel()
){
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    // Giám sát trạng thái lifecycle
    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> {
                    // Thiết lập một delay để gọi API sau 30 giây nếu người dùng không quay lại
                    coroutineScope.launch{
                        delay(30000)  // Delay 30 giây
                        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED).not()) {
                            realTimeViewModel.leaveRoom(roomId)
                        }
                    }
                }
                Lifecycle.Event.ON_START -> {
                    // Nếu người dùng quay trở lại, hủy các coroutine đang chờ xử lý
                    coroutineScope.coroutineContext.cancelChildren()
                }
                else -> {

                }
            }
        }

        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }


    LaunchedEffect(key1 = roomPin,key2 = roomId){
        realTimeViewModel.listenToRoomChanges(roomPin + roomId)
    }

    BackHandler {

    }

    val room by realTimeViewModel.roomDetail.collectAsState()
    val isHost by realTimeViewModel.isHost.collectAsState()
    val isLoading by realTimeViewModel.isLoading.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Xếp Hạng", "Đáp Án Của Bạn")

    Scaffold(
        topBar = {
            TopBar(
                title = "Phòng Chờ",
//                navController = navController
            )
        },
        bottomBar = {
            if(isHost == true){
                ButtonNavigate(
                    value = "Bắt Đầu",
                    onClick = {
//                        navController.navigate("${Routes.PLAY_QUIZ_SCREEN}/${roomm.quiz.id}/${roomm.id}")
                              // go to screen play game real time
                    },
                    color = MaterialTheme.colorScheme.primary
                )
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
                if(isLoading){
                    Loading()
                }else if (!isLoading && room != null){
                    WaitingImage(room!!.quiz.thumbnail)

                    Spacer(
                        modifier = Modifier.height(dimensionResource(id = R.dimen.space_app_normal))
                    )

                    CardHeading(room!!.quiz.title,room!!.users.size,room!!.maxUser)

                    TabRow(selectedTabIndex) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                text = { Text(title) }
                            )
                        }
                    }

                    BackHandler(viewModel = realTimeViewModel,roomId)

                    when (selectedTabIndex) {
                        0 -> UsersContent(room!!.users)
//                        1 -> AnswerContent(listAnswers)
                    }
                }
            }
        }
    )
}


@Composable
private fun UsersContent(users: HashMap<String,User>){
    val list = users.map { (key,value) ->
        User(
            id = value.id,
            displayName = value.displayName,
            avatar = value.avatar
        )
    }
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(list.size){ index ->
            val item = list[index]
            UserItem(
                user = item,
                index = index
            )
        }
    }
}

@Composable
private fun UserItem(index:Int,user: User){
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
//            Text(
//                text = "${history.totalScore}",
//                fontWeight = FontWeight.SemiBold,
//                color = MaterialTheme.colorScheme.primary
//            )
        }
    }
}


@Composable
private fun CardHeading(title:String,totalUser:Int,maxUser:Int){
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
private fun Answerd(index:Int,history: HistoryAnswer){
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
private fun AnswerContent(history: List<HistoryAnswer>){
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
fun BackHandler(viewModel: RealTimeViewModel,roomId:Int) {
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                setShowDialog(false)
            },
            title = { Text(text = "Xác nhận") },
            text = { Text(text = "Bạn có chắc chắn muốn thoát không?") },
            confirmButton = {
                Button(onClick = {
                    viewModel.leaveRoom(roomId)
                    setShowDialog(false)
                }) {
                    Text("Có")
                }
            },
            dismissButton = {
                Button(onClick = {

                    setShowDialog(false)
                }) {
                    Text("Không")
                }
            }
        )
    }
    BackHandler(enabled = true) {
        setShowDialog(true)
    }
}