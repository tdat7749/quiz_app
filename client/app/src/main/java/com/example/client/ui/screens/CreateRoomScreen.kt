package com.example.client.ui.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.client.R
import com.example.client.model.Room
import com.example.client.ui.components.*
import com.example.client.ui.viewmodel.CreateRoomViewModel
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import java.time.LocalDate
import java.time.LocalTime
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CreateRoomScreen(
    quizId:Int,
    title:String,
    thumbnail:String,
    navController: NavController,
    createRoomViewModel: CreateRoomViewModel = hiltViewModel()
){

    val create by createRoomViewModel.create.collectAsState()


    when(create){
        is ResourceState.Success -> {
            ShowMessage((create as ResourceState.Success<ApiResponse<Room>>).value.message)
            LaunchedEffect(Unit){
                navController.popBackStack()
            }
        }
        is ResourceState.Error -> {
            (create as ResourceState.Error).errorBody?.let { ShowMessage(it.message) { createRoomViewModel.resetState() } }
        }
        else -> {

        }
    }

    Scaffold(
        topBar = {
            TopBar(
                "Tạo Phòng Chơi",
                navController
            )
        },
        content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(id = R.dimen.padding_app))
                        .background(Color.White)
                        .verticalScroll(rememberScrollState())
                        .padding(it)
                ) {
                    QuizThumbnail(thumbnail)
                    QuestionSection(title)

                    TextFieldOutlined(
                        createRoomViewModel.roomName,
                        onChangeValue = {value ->
                            createRoomViewModel.onChangeRoomname(value)
                        },
                        "Tên Phòng",
                        painterResource(id = R.drawable.title)
                    )
                    Spacer(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.space_app_small))
                    )
                    CalendarComponent(
                        value = createRoomViewModel.timeStart.toString(),
                        label = "Ngày Bắt Đầu",
                        onChangeDate = { it ->
                            createRoomViewModel.onChangeTimeStart(it)
                        }
                    )
                    Spacer(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.space_app_small))
                    )
                    TimeComponent(
                        value = createRoomViewModel.timeStartClock.toString(),
                        label = "Giờ Bắt Đầu",
                        onChangeTime = {it ->
                           createRoomViewModel.onChangeTimeStartClock(it)
                        }
                    )
                    Spacer(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.space_app_normal))
                    )
                    CalendarComponent(
                        value = createRoomViewModel.timeEnd.toString(),
                        label = "Ngày Kết Thúc",
                        onChangeDate = { it ->
                            createRoomViewModel.onChangeTimeEnd(it)
                        }
                    )
                    Spacer(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.space_app_small))
                    )
                    TimeComponent(
                        value = createRoomViewModel.timeEndClock.toString(),
                        label = "Giờ Kết Thúc",
                        onChangeTime = {it ->
                            createRoomViewModel.onChangeTimeEndClock(it)
                        }
                    )
                    Spacer(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.space_app_normal))
                    )

                    ButtonComponent(
                        onClick = {
                            createRoomViewModel.onCreateRoom(quizId)
                        },
                        stringResource(id = R.string.register),
                        MaterialTheme.colorScheme.primary,
                        create is ResourceState.Loading,
                        create !is ResourceState.Loading
                    )
                }
        }
    )
}


@Composable
private fun QuizThumbnail(thumbnail:String?) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.quiz_card_height))
    ) {
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
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalendarComponent(value:String,label:String,date: LocalDate? = null, onChangeDate:(LocalDate) -> Unit){
    val calendarState = rememberUseCaseState()

    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
        ),
        selection = CalendarSelection.Date{
            onChangeDate(it)
        }
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(5f)) {
            OutlinedTextField(
                value = if(value == "null") "" else value,
                onValueChange = {  },
                label = {
                    Text(label)
                },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
        IconButton(modifier = Modifier.weight(1f), onClick = { calendarState.show() }) {
            Icon(
                Icons.Default.EditCalendar,
                modifier = Modifier.size(32.dp),
                contentDescription = null,
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeComponent(value:String,label:String,date: LocalTime? = null, onChangeTime:(LocalTime) -> Unit){
    val clockState = rememberUseCaseState()

    ClockDialog(
        state = clockState,
        config = ClockConfig(
            boundary = LocalTime.of(0, 0, 0)..LocalTime.of(12, 59, 0),
            is24HourFormat = true
        ),
        selection = ClockSelection.HoursMinutes{hours, minutes ->
            onChangeTime(LocalTime.of(hours,minutes))
        }
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(5f)) {
            OutlinedTextField(
                value = if(value == "null") "" else value,
                onValueChange = {  },
                label = {
                    Text(label)
                },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
        IconButton(modifier = Modifier.weight(1f), onClick = { clockState.show() }) {
            Icon(
                Icons.Default.EditCalendar,
                modifier = Modifier.size(32.dp),
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun QuestionSection(
    title: String,
) {
    Column {

        QuestionTitle(questionTitle = title)

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            thickness = 2.dp,
            color = Color.Gray
        )
    }
}

@Composable
private fun QuestionTitle(questionTitle: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        text = questionTitle,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Start,
        color = MaterialTheme.colorScheme.primary,
        fontSize = 26.sp
    )
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