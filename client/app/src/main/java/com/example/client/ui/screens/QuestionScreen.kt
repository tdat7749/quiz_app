package com.example.client.ui.screens

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.client.R
import com.example.client.model.QuestionType
import com.example.client.ui.components.*
import com.example.client.ui.viewmodel.CreateQuizViewModel
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import com.example.client.utils.Utilities


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun QuestionScreen (
    index:Int,
    navController: NavController,
    createQuizViewModel: CreateQuizViewModel
){

    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            createQuizViewModel.onChangeQuestionImage(uri)
            val path = Utilities.Companion.getRealPathFromURI(uri!!,context)
            if(path != null){
                createQuizViewModel.onChangeQuestionThumbnailPath(path)
            }
        }

    val types by createQuizViewModel.types.collectAsState()

    var isTrueAnswer by remember { mutableStateOf(-1) }

    if(index != -1){
        val value = createQuizViewModel.setValueQuestionByIndex(index)
        value.answers.forEachIndexed { i, createAnswer ->
            if(createAnswer.isCorrect){
                isTrueAnswer = i
            }
        }
    }

    DisposableEffect(Unit){
        onDispose {
            createQuizViewModel.resetQuestion()
            createQuizViewModel.resetAnswer()
        }
    }

    Scaffold (
        topBar = {
            TopBar(
                "",
                navController
            )
        },
        content = {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.padding_app))
                    .background(color = MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
                    .padding(it)
            ) {
                    when(types){
                        is ResourceState.Loading -> {
                            Loading()
                        }
                        is ResourceState.Success -> {
                            ImageCard(createQuizViewModel.qImageUri.value)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Button(
                                    onClick = { launcher.launch("image/*") },
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(text = "Chọn ảnh")
                                }
                            }

                            TextFieldOutlined(
                                createQuizViewModel.questionTitle,
                                onChangeValue = {value ->
                                    createQuizViewModel.onChangeQuestionTitle(value)
                                },
                                stringResource(id = R.string.title_question),
                                painterResource(id = R.drawable.person)
                            )
                            Spacer(
                                modifier = Modifier
                                    .height(dimensionResource(id = R.dimen.space_app_small))
                            )
                            ///

                            DropdownQuestionType(
                                options = (types as ResourceState.Success<ApiResponse<List<QuestionType>>>).value.data,
                                createQuizViewModel.questionType,
                                onChangeValue = {type ->
                                    createQuizViewModel.onChangeQuestionType(type)
                                }
                            )

                            NumberFieldOutlined(
                                createQuizViewModel.score,
                                onChangeValue = {value ->
                                    val newValue =  value.toIntOrNull() ?: 0
                                    createQuizViewModel.onChangeScore(newValue)
                                },
                                stringResource(id = R.string.score),
                                painterResource(id = R.drawable.score)
                            )
                            Spacer(
                                modifier = Modifier
                                    .height(dimensionResource(id = R.dimen.space_app_small))
                            )


                            NumberFieldOutlined(
                                createQuizViewModel.timeLimit,
                                onChangeValue = {value ->
                                    val newValue =  value.toIntOrNull() ?: 10
                                    createQuizViewModel.onChangeTimeLimit(newValue)
                                },
                                stringResource(id = R.string.time_limit),
                                painterResource(id = R.drawable.time)
                            )
                            Spacer(
                                modifier = Modifier
                                    .height(dimensionResource(id = R.dimen.space_app_small))
                            )

                            AnswersContent(
                                answer = {value ->
                                    createQuizViewModel.setAnswersFieldsValue(index = value)
                                },
                                isTrueAnswer = isTrueAnswer,
                                answerValueChanged = { value, index ->
                                    createQuizViewModel.updateAnswersFields(newValue = value, index = index)
                                },
                                onAnswerClick = {value ->
                                    isTrueAnswer = value
                                    createQuizViewModel.setTrueAnswer(value)
                                },
                            )

                            Spacer(
                                modifier = Modifier
                                    .height(dimensionResource(id = R.dimen.space_app_small))
                            )

                            if(index == -1){
                                ButtonNavigate(
                                    onClick = {
                                        createQuizViewModel.addQuestion()
                                        navController.popBackStack()
                                    },
                                    "Lưu Câu Hỏi",
                                    MaterialTheme.colorScheme.primary,
                                )
                            }else{
                                ButtonNavigate(
                                    onClick = {
                                        createQuizViewModel.editQuestion(index)
                                        navController.popBackStack()
                                    },
                                    "Sửa Câu Hỏi",
                                    MaterialTheme.colorScheme.primary,
                                )
                            }
                            Spacer(
                                modifier = Modifier
                                    .height(dimensionResource(id = R.dimen.space_app_small))
                            )
                            ButtonNavigate(
                                onClick = {
                                    navController.popBackStack()
                                },
                                "Hủy",
                                MaterialTheme.colorScheme.secondary,
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
private fun AnswersContent(
    answer: (Int) -> String,
    isTrueAnswer: Int,
    answerValueChanged: (String, Int) -> Unit,
    onAnswerClick: (Int) -> Unit
) {
    Text(
        text = "Nhập câu trả lời"
    )
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(4) { index ->
            Answer(
                modifier = Modifier,
                answer = answer,
                isTrueAnswer = isTrueAnswer,
                index = index,
                answerValueChanged = answerValueChanged,
                onAnswerClick = onAnswerClick
            )
        }
    }
}


@Composable
private fun Answer(
    modifier: Modifier,
    answer: (Int) -> String,
    isTrueAnswer: Int,
    index: Int,
    answerValueChanged: (String, Int) -> Unit,
    onAnswerClick: (Int) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = modifier.weight(5f)) {
            TextFieldOutlined(
                answer(index),
                onChangeValue = { answerValueChanged(it, index) },
                "Câu Trả Lời",
                painterResource(id = R.drawable.person)
            )
        }
        IconButton(modifier = modifier.weight(1f), onClick = { onAnswerClick(index) }) {
            Icon(
                Icons.Default.Check,
                modifier = modifier.size(32.dp),
                contentDescription = null,
                tint = if (index == isTrueAnswer) Color.Green else Color.Gray
            )
        }
    }
}