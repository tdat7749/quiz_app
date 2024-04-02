package com.example.client.ui.screens

import android.annotation.SuppressLint
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
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
            navController.navigate(Routes.QUIZ_RESULT_SCREEN)
        }
    }


    Scaffold(
        content = {
            Surface (
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
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
                                        playQuizViewModel.goNextQuestion()
                                        playQuizViewModel.finishQuiz(quizId, roomId)
                                    } else {
                                        playQuizViewModel.goNextQuestion()
                                        timer.value = questions[playQuizViewModel.questionIndex].timeLimit
                                    }
                                }
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(dimensionResource(id = R.dimen.padding_app))

                        ) {
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
                                    playQuizViewModel.goNextQuestion()
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

    AnswersSection(
        answers = quizData[questionIndex].answers,
        onChecked = onChecked,
        checked = {
            return@AnswersSection selectedAnswerId == it
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
private fun AnswersSection(
    answers: List<Answer>,
    onChecked: (Int) -> Unit,
    checked: (Int) -> Boolean
) {
    Column(
        modifier = Modifier.padding(top = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        answers.forEach {
            Answer(
                modifier = Modifier.padding(vertical = 8.dp),
                answerText = it.title,
                onChecked = { onChecked(it.id) },
                checked = checked(it.id)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Answer(
    modifier: Modifier,
    answerText: String,
    onChecked: () -> Unit,
    checked: Boolean
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(TextFieldDefaults.MinHeight + 8.dp)
            .border(
                border = BorderStroke(width = 1.dp, color = Color.Black),
                shape = RoundedCornerShape(25)
            )
            .background(color = MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(25))
            .clickable(enabled = true, onClick = {}),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = modifier.padding(start = 16.dp),
            text = answerText,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp
        )
        CircleCheckBox(
            onChecked = onChecked,
            selected = checked
        )
    }
}

@Composable
private fun ThumbnailQuestion(thumbnail:String?) {
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

@Composable
private fun QuestionSection(
    title: String,
    time:Int,
    score:Int
) {
    Column {
        Timer(time)
        Spacer(
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.space_app_normal))
        )
        Score(score)
        Spacer(
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.space_app_normal))
        )
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
private fun Timer(time: Int) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        text = "Thòi Gian: ${time.toString()}",
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Start,
        color = MaterialTheme.colorScheme.primary,
        fontSize = 26.sp
    )
}

@Composable
private fun Score(score: Int) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        text = "Điểm Câu Hỏi: ${score.toString()}",
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Start,
        color = MaterialTheme.colorScheme.primary,
        fontSize = 26.sp
    )
}
