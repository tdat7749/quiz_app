package com.example.client.ui.components.room

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.client.R
import com.example.client.model.Quiz
import com.example.client.model.Room
import com.example.client.ui.components.HeadingBoldText
import com.example.client.ui.components.NormalText
import com.example.client.ui.navigation.Routes
import com.example.client.ui.screens.UserInfo

@Composable
fun RoomCard(room: Room, navController: NavController,isHost:Boolean = false){
    Box(){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.quiz_card_height))
                .width(dimensionResource(id = R.dimen.quiz_card_height))
                .shadow(4.dp,shape = RoundedCornerShape(8.dp)),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
            onClick = {
                if(!isHost){
                    navController.navigate("${Routes.WAITING_ROOM_SCREEN}/${room.roomPin}/${room.id}")
                }else{
                    navController.navigate("${Routes.ROOM_DETAIL_SCREEN}/${room.id}")
                }
            }
        ){
            Column (
                modifier = Modifier
                    .fillMaxSize()
            ) {
                AsyncImage(
                    model = room.quiz.thumbnail,
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
                ){
                    HeadingBoldText(
                        room.roomName,
                        TextAlign.Start,
                        MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .fillMaxWidth(),
                        fontSize = 20.sp
                    )

                    NormalText(
                        "PIN: ${room.roomPin}",
                        TextAlign.Start,
                        MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth(),
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun RoomName(name: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth(),
        text = name,
        fontSize = 32.sp,
        textAlign = TextAlign.Start,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun RoomPin(pin: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.pin),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            text = "Pin: $pin",
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun PlayAgain(isPlayAgain: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.done),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            text = "Luật Chơi: ${if(isPlayAgain) "Cho Chơi Lại" else "Không Cho Chơi Lại"}",
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun UserNumber(totalUser:Int,maxUser:Int){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.group),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            text = "Người Chơi: ${totalUser.toString()}/${maxUser.toString()}",
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun TimeRoom(
    timeStart: String?,
    timeEnd: String?,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        if(timeStart != null){
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = "Thời Gian Bắt Đầu: ${timeStart}",
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        if(timeEnd != null){
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = "Thời Gian Kết Thúc: ${timeEnd}",
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun RoomStatus(title:String,status: Boolean) {
    Row (
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = "$title: " + if(status) "Đã Đóng" else "Đang Mở",
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}