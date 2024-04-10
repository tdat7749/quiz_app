package com.example.client.ui.screens

import android.annotation.SuppressLint
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.client.R
import com.example.client.model.Answer
import com.example.client.model.Answered
import com.example.client.model.QuestionDetail
import com.example.client.ui.components.ButtonComponent
import com.example.client.ui.components.ButtonNavigate
import com.example.client.ui.components.CircleCheckBox
import com.example.client.ui.components.Loading
import com.example.client.ui.navigation.Routes
import com.example.client.ui.viewmodel.PlayQuizViewModel
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import kotlinx.coroutines.delay

val listBg = arrayListOf(
    Color(0xFFe21b3d),
    Color(0xFF1268cf),
    Color(0xFFd89e01),
    Color(0xFF25880c)
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayQuizScreen(
    quizId:Int,
    roomId:Int?,
    navController: NavController,
    playQuizViewModel: PlayQuizViewModel
){

    LaunchedEffect(Unit){
        playQuizViewModel.getListQuestionByQuizId(quizId)
    }

    val startQuizState by playQuizViewModel.startQuizState.collectAsState()

    val saveResult by playQuizViewModel.saveResult.collectAsState()

    var selectedAnswerId by rememberSaveable { mutableStateOf(-1) }

    DisposableEffect(Unit){
        onDispose {
            playQuizViewModel.resetFlowState()
        }
    }

    if(saveResult is ResourceState.Success){
        LaunchedEffect(Unit){
            navController.navigate("${Routes.QUIZ_RESULT_SCREEN}/${quizId}/${roomId.toString()}")
        }
    }


    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.padding_app))
                    .background(color = MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
                    .padding(it)

            ) {
                when(startQuizState){
                    is ResourceState.Loading -> {
                        Loading()
                    }
                    is ResourceState.Success -> {
                        val questions = (startQuizState as ResourceState.Success<ApiResponse<List<QuestionDetail>>>).value.data

                        val timer = remember { mutableStateOf(questions[playQuizViewModel.questionIndex].timeLimit) }

                        fun onChangeTimer(newValue: Int){ timer.value = newValue}

                        LaunchedEffect(timer.value) {
                            while (timer.value > 0) {
                                delay(1000)
                                timer.value--
                                if (timer.value == 0) {
                                    if (playQuizViewModel.isLastQuestion()) {
                                        playQuizViewModel.goNextQuestion(true,questions[playQuizViewModel.questionIndex].id)
                                        playQuizViewModel.finishQuiz(quizId, roomId)
                                    } else {
                                        playQuizViewModel.goNextQuestion(true,questions[playQuizViewModel.questionIndex].id)
                                        timer.value = questions[playQuizViewModel.questionIndex].timeLimit
                                    }
                                }
                            }
                        }

                            QuizSection(
                                questionIndex = playQuizViewModel.questionIndex,
                                quizData = questions,
                                viewModel = playQuizViewModel,
                                selectedAnswerId = selectedAnswerId,
                                onChecked = {
                                    selectedAnswerId = it
                                },
                                onNextClicked = {list ->
                                    var currentQuestion = list[playQuizViewModel.questionIndex]
                                    playQuizViewModel.addSelectAnswer(
                                        answer = Answered(
                                            questionId = currentQuestion.id,
                                            answerId = currentQuestion.answers.find { item ->
                                                item.id == selectedAnswerId
                                            }?.id,
                                            isCorrect = if(currentQuestion.answers.find { item ->
                                                    item.id == selectedAnswerId
                                                }?.correct == true) true else false,
                                            score = if(currentQuestion.answers.find { item ->
                                                    item.id == selectedAnswerId
                                                }?.correct == true) currentQuestion.score else 0
                                        )
                                    )
                                    selectedAnswerId = -1
                                    playQuizViewModel.goNextQuestion(false,questions[playQuizViewModel.questionIndex].id)
                                },
                                saveResult = saveResult,
                                quizId = quizId,
                                roomId= roomId,
                                timer = timer.value,
                                onChangeTimer = { value ->
                                    onChangeTimer(value)
                                }
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
private fun QuizSection(
    questionIndex: Int,
    quizData: List<QuestionDetail>,
    viewModel: PlayQuizViewModel,
    selectedAnswerId: Int,
    onChecked: (Int) -> Unit,
    onNextClicked: (List<QuestionDetail>) -> Unit,
    saveResult: ResourceState<ApiResponse<Boolean>>,
    quizId: Int,
    roomId:Int?,
    timer:Int,
    onChangeTimer:(Int) -> Unit
) {
    Spacer(
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.space_app_normal))
    )
    ThumbnailQuestion(quizData[questionIndex].thumbnail)

    Spacer(
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.space_app_normal))
    )

    QuestionSection(
        quizData[questionIndex].title,
        timer,
        quizData[questionIndex].score
    )

    FactsGrid(
        answers = quizData[questionIndex].answers,
        onChecked = onChecked,
        checked = {
            return@FactsGrid selectedAnswerId == it
        }
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ButtonComponent(
            onClick = {
                if (viewModel.isLastQuestion()) {
                    onNextClicked(quizData)
                    viewModel.finishQuiz(quizId = quizId,roomId = roomId)
                } else {
                    onNextClicked(quizData)
                    onChangeTimer(quizData[questionIndex].timeLimit)
                }
            },
            value = if (viewModel.isLastQuestion()) "Hoàn Thành" else "Câu Kế Tiếp",
            color = MaterialTheme.colorScheme.primary,
            loading = saveResult is ResourceState.Loading,
            enable = saveResult !is ResourceState.Loading
        )
    }
}

@Composable
private fun ThumbnailQuestion(thumbnail:String?) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        AsyncImage(
            model = thumbnail,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}

@Composable
private fun QuestionSection(
    title: String,
    time:Int,
    score:Int
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Timer(time)
        Spacer(
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.space_app_small))
        )
        Score(score)
        Spacer(
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.space_app_small))
        )
        QuestionTitle(questionTitle = title)
    }
}

@Composable
private fun QuestionTitle(questionTitle: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 26.dp, bottom = 20.dp),
        text = questionTitle,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Start,
        color = MaterialTheme.colorScheme.primary,
        fontSize = 20.sp
    )
}

@Composable
private fun Timer(time: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier =  Modifier.size(20.dp),
            painter = painterResource(id = R.drawable.time),
            contentDescription = "timer",
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(
            modifier = Modifier.width(6.dp)
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = time.toString(),
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp
        )
    }
}

@Composable
private fun Score(score: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.score),
            contentDescription = "score",
            modifier =  Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(
            modifier = Modifier.width(6.dp)
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = score.toString(),
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp
        )
    }
}

@Composable
fun FactsGrid(
    answers: List<Answer>,
    onChecked: (Int) -> Unit,
    checked: (Int) -> Boolean
) {

    Column(
        modifier = Modifier.padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        answers.forEachIndexed() {index,it ->
            FactBox(
                answerText = it.title,
                onChecked = { onChecked(it.id) },
                checked = checked(it.id),
                index = index
            )
            Spacer(
                modifier = Modifier.height(6.dp)
            )
        }
    }
}

@Composable
fun FactBox(
    answerText: String,
    onChecked: () -> Unit,
    checked: Boolean,
    index:Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = (listBg[index])
        )
    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Text(
                text = answerText,
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold
            )
            CircleCheckBox(
                onChecked = onChecked,
                selected = checked
            )
        }
    }
}