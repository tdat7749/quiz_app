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
import com.example.client.model.Answer
import com.example.client.model.QuestionDetail
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
    val editQuestion by editQuizViewModel.editQuestion.collectAsState()
    val changeQuestionThumbnail by editQuizViewModel.changeQuestionThumbnail.collectAsState()
    val createQuestion by editQuizViewModel.createQuestion.collectAsState()
    val editAnswer by editQuizViewModel.editAnswer.collectAsState()

    when(editQuestion){
        is ResourceState.Success ->{
            ShowMessage(
                message = (editQuestion as ResourceState.Success<ApiResponse<QuestionDetail>>).value.message,
                onReset = {
                    editQuizViewModel.resetEditQuestionState()
                }
            )
        }
        is ResourceState.Error ->{
            (editQuestion as ResourceState.Error).errorBody?.let {
                ShowMessage(
                    message = it.message,
                    onReset = {
                        editQuizViewModel.resetEditQuestionState()
                    }
                )
            }
        }
        else ->{

        }
    }

    when(editAnswer){
        is ResourceState.Success ->{
            ShowMessage(
                message = (editAnswer as ResourceState.Success<ApiResponse<List<Answer>>>).value.message,
                onReset = {
                    editQuizViewModel.resetEditAnswerState()
                }
            )
        }
        is ResourceState.Error ->{
            (editAnswer as ResourceState.Error).errorBody?.let {
                ShowMessage(
                    message = it.message,
                    onReset = {
                        editQuizViewModel.resetEditAnswerState()
                    }
                )
            }
        }
        else ->{

        }
    }

    when(createQuestion){
        is ResourceState.Success ->{
            ShowMessage(
                message = (createQuestion as ResourceState.Success<ApiResponse<QuestionDetail>>).value.message,
                onReset = {
                    editQuizViewModel.resetCreateQuestionState()
                }
            )
            LaunchedEffect(Unit){
                navController.popBackStack()
            }
        }
        is ResourceState.Error ->{
            (createQuestion as ResourceState.Error).errorBody?.let {
                ShowMessage(
                    message = it.message,
                    onReset = {
                        editQuizViewModel.resetCreateQuestionState()
                    }
                )
            }
        }
        else ->{

        }
    }

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }

    var selectedImgUri by remember { mutableStateOf("") }

    val context = LocalContext.current

    val launcherEdit =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->

            if(uri != null){
                selectedImgUri = uri.toString()
                val path = Utilities.getRealPathFromURI(uri, context)
                if (path != null) {
                    editQuizViewModel.resetChangeThumbnailState()
                    editQuizViewModel.onChangeBottomSheetQuestionThumbnail(path)
                    openBottomSheet = true
                }
            }
        }

    val launcherCreate =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            editQuizViewModel.onChangeQuestionImage(uri)
            val path = Utilities.Companion.getRealPathFromURI(uri!!,context)
            if(path != null){
                editQuizViewModel.onChangeQuestionThumbnailPath(path)
            }
        }

    val coroutineScope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(
        confirmValueChange = { it != SheetValue.Expanded }
    )

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }

    DisposableEffect(Unit){
        onDispose {
            editQuizViewModel.resetQuestion()
            editQuizViewModel.resetAnswer()
        }
    }


    Scaffold (
        topBar = {
            TopBar(
                "Cập Nhật Câu Hỏi",
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
                        if(index != -1){
                            LandingImage(editQuizViewModel.questionThumbnail)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Button(
                                    onClick = { launcherEdit.launch("image/*") },
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(text = "Chọn ảnh")
                                }
                            }
                        }else{
                            ImageCard(editQuizViewModel.qImageUri.value)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Button(
                                    onClick = { launcherCreate.launch("image/*") },
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(text = "Chọn ảnh")
                                }
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

                        if(index != -1){
                            ButtonComponent(
                                onClick = {
                                    editQuizViewModel.editQuestion(quizId,index)
                                },
                                value = "Sửa Câu Hỏi",
                                color = MaterialTheme.colorScheme.primary,
                                loading =  editQuestion is ResourceState.Loading,
                                enable = editQuestion !is ResourceState.Loading
                            )
                        }

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
                            ButtonComponent(
                                onClick = {
                                    editQuizViewModel.addQuestion(quizId)
                                },
                                value= "Thêm Câu Hỏi",
                                color = MaterialTheme.colorScheme.primary,
                                loading =  createQuestion is ResourceState.Loading,
                                enable = createQuestion !is ResourceState.Loading
                            )
                        }

                        if(index != -1){
                            ButtonComponent(
                                onClick = {
                                    editQuizViewModel.editAnswer(quizId,index)
                                },
                                value = "Sửa Câu Trả Lời",
                                color = MaterialTheme.colorScheme.primary,
                                loading =  editAnswer is ResourceState.Loading,
                                enable = editAnswer !is ResourceState.Loading
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

                        if (openBottomSheet) {
                            ModalBottomSheet(
                                onDismissRequest = {
                                    openBottomSheet = false
                                    editQuizViewModel.resetChangeQuestionThumbnailState()
                                },
                                modifier = Modifier.fillMaxSize(),
                                content = {
                                    SheetThumbnailContent(
                                        thumbnailUrl = selectedImgUri,
                                        coroutineScope = coroutineScope,
                                        sheetState = sheetState,
                                        openBottomSheet = {value ->
                                            openBottomSheet = value
                                        },
                                        changeThumbnailState = changeQuestionThumbnail,
                                        editQuizViewModel = editQuizViewModel,
                                        quizId = quizId,
                                        questionId = editQuizViewModel.questionId
                                    )
                                },
                                sheetState = sheetState,
                                containerColor = MaterialTheme.colorScheme.background
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
    quizId:Int,
    questionId:Int
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        UploadImageSection(
            modifier = Modifier,
            thumbnailUrl = thumbnailUrl,
            onSaveImgClick = {
                editQuizViewModel.editQuestionThumbnail(questionId,quizId)
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
