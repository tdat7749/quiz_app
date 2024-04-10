import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.client.ui.viewmodel.EditRoomViewModel
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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditRoomScreen(
    id:Int,
    navController:NavController,
    editRoomViewModel:EditRoomViewModel = hiltViewModel()
){
    val edit by editRoomViewModel.edit.collectAsState()
    val room by editRoomViewModel.room.collectAsState()


    LaunchedEffect(key1 = id){
        editRoomViewModel.getRoom(id)
    }

    when(edit){
        is ResourceState.Success -> {
            ShowMessage((edit as ResourceState.Success<ApiResponse<Boolean>>).value.message)
        }
        is ResourceState.Error -> {
            (edit as ResourceState.Error).errorBody?.let { ShowMessage(it.message) { editRoomViewModel.resetState() } }
        }
        else -> {

        }
    }


    Scaffold(
        topBar = {
            TopBar(
                "Cập Nhật Phòng Chơi",
                navController
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.padding_app))
                    .background(color = MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
                    .padding(it)
            ) {
                when(room){
                    is ResourceState.Loading -> {
                        Loading()
                    }
                    is ResourceState.Success -> {
                        val room = (room as ResourceState.Success<ApiResponse<Room>>).value.data
                        QuizThumbnail(room.quiz.thumbnail)
                        QuestionSection(room.quiz.title)

                        TextFieldOutlined(
                            editRoomViewModel.roomName,
                            onChangeValue = {value ->
                                editRoomViewModel.onChangeRoomname(value)
                            },
                            "Tên Phòng",
                            painterResource(id = R.drawable.title)
                        )
                        Spacer(
                            modifier = Modifier
                                .height(dimensionResource(id = R.dimen.space_app_small))
                        )
                        NumberFieldOutlined(
                            editRoomViewModel.maxUser,
                            onChangeValue = {value ->
                                editRoomViewModel.onChangeMaxUser(Integer.parseInt(value))
                            },
                            "Số Thành Viên Tối Đa",
                            painterResource(id = R.drawable.title)
                        )
                        Spacer(
                            modifier = Modifier
                                .height(dimensionResource(id = R.dimen.space_app_small))
                        )
                        CalendarComponent(
                            value = editRoomViewModel.timeStart.toString(),
                            label = "Ngày Bắt Đầu",
                            onChangeDate = { it ->
                                editRoomViewModel.onChangeTimeStart(it)
                            }
                        )
                        Spacer(
                            modifier = Modifier
                                .height(dimensionResource(id = R.dimen.space_app_small))
                        )
                        TimeComponent(
                            value = editRoomViewModel.timeStartClock.toString(),
                            label = "Giờ Bắt Đầu",
                            onChangeTime = { it ->
                                editRoomViewModel.onChangeTimeStartClock(it)
                            }
                        )
                        Spacer(
                            modifier = Modifier
                                .height(dimensionResource(id = R.dimen.space_app_normal))
                        )
                        CalendarComponent(
                            value = editRoomViewModel.timeEnd.toString(),
                            label = "Ngày Kết Thúc",
                            onChangeDate = { it ->
                                editRoomViewModel.onChangeTimeEnd(it)
                            }
                        )
                        Spacer(
                            modifier = Modifier
                                .height(dimensionResource(id = R.dimen.space_app_small))
                        )
                        TimeComponent(
                            value = editRoomViewModel.timeEndClock.toString(),
                            label = "Giờ Kết Thúc",
                            onChangeTime = { it ->
                                editRoomViewModel.onChangeTimeEndClock(it)
                            }
                        )

                        Spacer(
                            modifier = Modifier
                                .height(dimensionResource(id = R.dimen.space_app_small))
                        )

                        SwitchLabel(
                            "Kết Thúc: ",
                            editRoomViewModel.isClosed,
                            onChangeValue = {value ->
                                editRoomViewModel.onChangeClosed(value)
                            }
                        )

                        Spacer(
                            modifier = Modifier
                                .height(dimensionResource(id = R.dimen.space_app_small))
                        )
                        SwitchLabel(
                            "Cho Phép Chơi Lại: ",
                            editRoomViewModel.isPlayAgain,
                            onChangeValue = {value ->
                                editRoomViewModel.onChangePlayAgain(value)
                            }
                        )

                        Spacer(
                            modifier = Modifier
                                .height(dimensionResource(id = R.dimen.space_app_normal))
                        )

                        ButtonComponent(
                            onClick = {
                                editRoomViewModel.onEditRoom(id)
                            },
                            "Cập Nhật Phòng Chơi",
                            MaterialTheme.colorScheme.primary,
                            edit is ResourceState.Loading,
                            edit !is ResourceState.Loading
                        )
                    }
                    else -> {

                    }
                }
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
private fun CalendarComponent(value:String, label:String, date: LocalDate? = null, onChangeDate:(LocalDate?) -> Unit){
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
                modifier = Modifier.fillMaxWidth(),
                suffix = {
                    IconButton(onClick = { onChangeDate(null) }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = null,
                        )
                    }
                }
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
private fun TimeComponent(value:String, label:String, date: LocalTime? = null, onChangeTime:(LocalTime?) -> Unit){
    val clockState = rememberUseCaseState()

    ClockDialog(
        state = clockState,
        config = ClockConfig(
            boundary = LocalTime.of(0, 0, 0)..LocalTime.of(12, 59, 0),
            is24HourFormat = true
        ),
        selection = ClockSelection.HoursMinutes{ hours, minutes ->
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
                modifier = Modifier.fillMaxWidth(),
                suffix = {
                    IconButton(onClick = { onChangeTime(null) }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = null,
                        )
                    }
                }
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