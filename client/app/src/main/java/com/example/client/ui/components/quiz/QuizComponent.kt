package com.example.client.ui.components.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import coil.compose.AsyncImage
import com.example.client.R

@Composable
fun LandingImage(thumbnail:String?) {
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
fun QuizName(quizName: String) {
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
fun QuizAuthorNameAndCreatedDate(
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
//        Text(
//            modifier = Modifier.padding(horizontal = 16.dp),
//            text = "-",
//            fontWeight = FontWeight.SemiBold,
//            color = MaterialTheme.colorScheme.primary
//        )
//        Icon(
//            painter = painterResource(id = R.drawable.calendar),
//            contentDescription = null,
//            tint = MaterialTheme.colorScheme.primary
//        )
//        Text(
//            modifier = Modifier.padding(start = 8.dp),
//            text = createdDate,
//            fontWeight = FontWeight.Normal,
//            textAlign = TextAlign.Start,
//            color = MaterialTheme.colorScheme.primary
//        )
    }
}

@Composable
fun QuizCategoryName(
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
fun QuizDescription(title:String,quizDescription: String) {
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
fun QuizStatus(title:String,status: Boolean) {
    Row (
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = "$title: " + if(status) "Công Khai" else "Riêng Tư",
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}