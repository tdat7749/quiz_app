package com.example.client.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.client.R
import com.example.client.model.Quiz
import com.example.client.model.Topic
import com.example.client.model.User
import com.example.client.ui.components.HeadingBoldText
import com.example.client.ui.theme.Shapes

@Composable
fun HomeScreen(){
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(dimensionResource(id = R.dimen.padding_app)),
        color = Color.White
    ) {
        val topic = Topic(
            1,
            "Lịch Sử",
            "a",
            "a",
            "a",
            "a"
        )
        val quiz = Quiz(
            1,
            "Quiz Hell",
            "quiz-hell",
            "asdasd",
            User(
                1,
                "Thien Dat",
                "ab"
            )
        )
        val listTopic = listOf(
            Topic(1, "Lịch Sử", "a", "a", "a", "a"),
            Topic(1, "Lịch Sử", "a", "a", "a", "a"),
            Topic(1, "Lịch Sử", "a", "a", "a", "a"),
            Topic(1, "Lịch Sử", "a", "a", "a", "a"),
            Topic(1, "Lịch Sử", "a", "a", "a", "a"),
            Topic(1, "Lịch Sử", "a", "a", "a", "a")
        )

        val listQuiz = listOf(
            Quiz(1, "Quiz Hell", "quiz-hell", "asdasd", User(1, "Thien Dat", "ab")),
            Quiz(1, "Quiz Hell", "quiz-hell", "asdasd", User(1, "Thien Dat", "ab")),
            Quiz(1, "Quiz Hell", "quiz-hell", "asdasd", User(1, "Thien Dat", "ab")),
            Quiz(1, "Quiz Hell", "quiz-hell", "asdasd", User(1, "Thien Dat", "ab")),
            Quiz(1, "Quiz Hell", "quiz-hell", "asdasd", User(1, "Thien Dat", "ab")),
            Quiz(1, "Quiz Hell", "quiz-hell", "asdasd", User(1, "Thien Dat", "ab"))
        )
        Column {
            SectionTopic("Chủ Đề",listTopic)
            SectionQuiz("Quiz Hay",listQuiz)
        }
    }
}

@Composable
fun TopicCard(topic: Topic){
    Box{
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
                    model = "https://www.proprofs.com/quiz-school/topic_images/p191f89lnh17hs1qnk9fj1sm113b3.jpg",
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
fun QuizCard(quiz:Quiz){
    Box(){
        Card(
            modifier = Modifier
                .fillMaxWidth()
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
                ){
                    HeadingBoldText(
                        quiz.title,
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
fun SectionTopic(title:String,items: List<Topic>){
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
                TopicCard(item)
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

@Preview
@Composable
fun HomeScreenPreview(){
    HomeScreen()
}