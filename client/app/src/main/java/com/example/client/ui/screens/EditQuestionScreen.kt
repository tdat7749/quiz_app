package com.example.client.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.client.R
import com.example.client.model.QuestionType
import com.example.client.ui.components.*
import com.example.client.ui.components.quiz.LandingImage
import com.example.client.ui.viewmodel.EditQuizViewModel
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import com.example.client.utils.Utilities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditQuestionScreen(
    quizId:Int,
    index:Int,
    navController: NavController,
    editQuizViewModel: EditQuizViewModel
){

    var isTrueAnswer by remember { mutableStateOf(-1) }

    if(index != -1){
        val value = editQuizViewModel.setValueQuestionByIndex(index)
        value.answers.forEachIndexed { i, answer ->
            if(answer.correct){
                isTrueAnswer = i
            }
        }
    }

    val types by editQuizViewModel.types.collectAsState()

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }

    var selectedImgUri by remember { mutableStateOf("") }

    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->

            if(uri != null){
                selectedImgUri = uri.toString()
                val path = Utilities.getRealPathFromURI(uri, context)
                if (path != null) {
                    editQuizViewModel.resetChangeThumbnailState()
                    editQuizViewModel.onChangeBottomSheetThumbnail(path)
                    openBottomSheet = true
                }
            }
        }

    val coroutineScope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(
        confirmValueChange = { it != SheetValue.Expanded }
    )

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
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
                    .background(Color.White)
                    .verticalScroll(rememberScrollState())
                    .padding(it)
            ) {
                when(types){
                    is ResourceState.Loading -> {
                        Loading()
                    }
                    is ResourceState.Success -> {
                        LandingImage(editQuizViewModel.questionThumbnailPath)

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
                            editQuizViewModel.questionTitle,
                            onChangeValue = {value ->
                                editQuizViewModel.onChangeQuestionTitle(value)
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
                            editQuizViewModel.questionType,
                            onChangeValue = {type ->
                                editQuizViewModel.onChangeQuestionType(type)
                            }
                        )

                        NumberFieldOutlined(
                            editQuizViewModel.score,
                            onChangeValue = {value ->
                                val newValue =  value.toIntOrNull() ?: 0
                                editQuizViewModel.onChangeScore(newValue)
                            },
                            stringResource(id = R.string.score),
                            painterResource(id = R.drawable.score)
                        )
                        Spacer(
                            modifier = Modifier
                                .height(dimensionResource(id = R.dimen.space_app_small))
                        )


                        NumberFieldOutlined(
                            editQuizViewModel.timeLimit,
                            onChangeValue = {value ->
                                val newValue =  value.toIntOrNull() ?: 10
                                editQuizViewModel.onChangeTimeLimit(newValue)
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
                                editQuizViewModel.setAnswersFieldsValue(index = value)
                            },
                            isTrueAnswer = isTrueAnswer,
                            answerValueChanged = { value, index ->
                                editQuizViewModel.updateAnswersFields(newValue = value, index = index)
                            },
                            onAnswerClick = {value ->
                                isTrueAnswer = value
                                editQuizViewModel.setTrueAnswer(value)
                            },
                        )

                        Spacer(
                            modifier = Modifier
                                .height(dimensionResource(id = R.dimen.space_app_small))
                        )

                        if(index == -1){
                            ButtonNavigate(
                                onClick = {
                                    editQuizViewModel.addQuestion(quizId)
                                    navController.popBackStack()
                                },
                                "Thêm Câu Hỏi",
                                MaterialTheme.colorScheme.primary,
                            )
                        }else{
                            ButtonNavigate(
                                onClick = {
                                    editQuizViewModel.editQuestion(quizId,index)
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
private fun UploadImageSection(
    modifier: Modifier,
    thumbnailUrl: String,
    onSaveImgClick: () -> Unit,
    onCancelImgClick: () -> Unit,
    changeAvatarState: ResourceState<ApiResponse<String>>
) {
    Column(
        modifier = modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.onBackground)
            .padding(dimensionResource(id = R.dimen.padding_app)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        AsyncImage(
            model = thumbnailUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.quiz_card_height))
                .shadow(4.dp,shape = RoundedCornerShape(8.dp))
        )
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalArrangement =  Arrangement.spacedBy(16.dp)
            ) {
                ButtonComponent(
                    onClick = onSaveImgClick,
                    value = "Lưu",
                    color = MaterialTheme.colorScheme.primary,
                    loading = changeAvatarState is ResourceState.Loading,
                    enable = changeAvatarState !is ResourceState.Loading
                )
                ButtonNavigate(
                    onClick = onCancelImgClick,
                    value = "Hủy",
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SheetThumbnailContent(
    thumbnailUrl: String,
    changeThumbnailState: ResourceState<ApiResponse<String>>,
    coroutineScope: CoroutineScope,
    sheetState: SheetState,
    openBottomSheet: (Boolean) -> Unit,
    editQuizViewModel: EditQuizViewModel,
    quizId:Int
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        UploadImageSection(
            modifier = Modifier,
            thumbnailUrl = thumbnailUrl,
            onSaveImgClick = {
                editQuizViewModel.changeThumbnail(quizId)
                coroutineScope.launch {
                    if(changeThumbnailState is ResourceState.Success){
                        sheetState.hide()
                        openBottomSheet(false)
                    }
                }
            },
            onCancelImgClick = {
                coroutineScope.launch {
                    sheetState.hide()
                    openBottomSheet(false)
                }
            },
            changeAvatarState = changeThumbnailState
        )
    }
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
