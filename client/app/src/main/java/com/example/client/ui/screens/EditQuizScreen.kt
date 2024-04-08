package com.example.client.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.client.R
import com.example.client.model.*
import com.example.client.ui.components.*
import com.example.client.ui.components.quiz.LandingImage
import com.example.client.ui.navigation.Routes
import com.example.client.ui.viewmodel.CreateQuizViewModel
import com.example.client.ui.viewmodel.EditQuizViewModel
import com.example.client.ui.viewmodel.UpdateProfileViewModel
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import com.example.client.utils.Utilities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private val EDIT_PROFILE_IMG_SIZE = 176.dp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditQuizScreen(
    id:Int,
    navController: NavController,
    editQuizViewModel: EditQuizViewModel
){

    val quiz by editQuizViewModel.quiz.collectAsState()
    val questions by editQuizViewModel.question.collectAsState()
    val topics by editQuizViewModel.topics.collectAsState()
    val changeThumbnail by editQuizViewModel.changeThumbnail.collectAsState()
    val editQuiz by editQuizViewModel.editQuiz.collectAsState()
    val listQuestions by editQuizViewModel.listQuestion.collectAsState()
    val deleteQuestion by editQuizViewModel.deleteQuestion.collectAsState()

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }

    var selectedImgUri by remember { mutableStateOf("") }


    if(quiz is ResourceState.Error || questions is ResourceState.Error || topics is ResourceState.Error) {
        navController.popBackStack()
    }

    when(changeThumbnail){
        is ResourceState.Success -> {
            ShowMessage((changeThumbnail as ResourceState.Success<ApiResponse<String>>).value.message)
        }
        is ResourceState.Error -> {
            (changeThumbnail as ResourceState.Error).errorBody?.message?.let {
                ShowMessage(
                    message = it,
                    onReset = {
                        editQuizViewModel.resetChangeThumbnailState()
                    })
            }
        }
        else -> {

        }
    }

    when(editQuiz){
        is ResourceState.Success -> {
            ShowMessage((editQuiz as ResourceState.Success<ApiResponse<Quiz>>).value.message)
        }
        is ResourceState.Error -> {
            (editQuiz as ResourceState.Error).errorBody?.message?.let {
                ShowMessage(
                    message = it,
                    onReset = {
                        editQuizViewModel.resetEditQuizState()
                    })
            }
        }
        else -> {

        }
    }

    when(deleteQuestion){
        is ResourceState.Success -> {
            ShowMessage((deleteQuestion as ResourceState.Success<ApiResponse<Boolean>>).value.message)
        }
        is ResourceState.Error -> {
            (deleteQuestion as ResourceState.Error).errorBody?.message?.let {
                ShowMessage(
                    message = it,
                    onReset = {
                        editQuizViewModel.resetDeleteeQuestionState()
                    })
            }
        }
        else -> {

        }
    }


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

    LaunchedEffect(key1 = id){
        editQuizViewModel.getQuiz(id)
        editQuizViewModel.getQuestion(id)
    }

    val coroutineScope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(
        confirmValueChange = { it != SheetValue.Expanded }
    )

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }

    Scaffold(
        topBar = {
            TopBar(
                "Cập Nhật Quiz",
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
                if(quiz is ResourceState.Loading || questions is ResourceState.Loading || topics is ResourceState.Loading){
                    Loading()
                }else if (quiz is ResourceState.Success && questions is ResourceState.Success && topics is ResourceState.Success){
                    LandingImage(editQuizViewModel.thumbnail)

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

                    DropdownTopicMenu(
                        options = (topics as ResourceState.Success<ApiResponse<List<Topic>>>).value.data,
                        editQuizViewModel.topic,
                        onChangeValue = {topic ->
                            editQuizViewModel.onChangeTopic(topic)
                        }
                    )

                    TextFieldOutlined(
                        editQuizViewModel.title,
                        onChangeValue = {value ->
                            editQuizViewModel.onChangeTitle(value)
                        },
                        "Tên Quiz",
                        painterResource(id = R.drawable.title)
                    )
                    Spacer(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.space_app_small))
                    )

                    TextFieldOutlined(
                        editQuizViewModel.summary,
                        onChangeValue = {value ->
                            editQuizViewModel.onChangeSummary(value)
                        },
                        "Giới Thiệu Ngắn",
                        painterResource(id = R.drawable.summary)
                    )
                    Spacer(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.space_app_small))
                    )

                    TextFieldOutlined(
                        editQuizViewModel.description,
                        onChangeValue = {value ->
                            editQuizViewModel.onChangeDescription(value)
                        },
                        "Giới Thiệu Chi Tiết",
                        painterResource(id = R.drawable.description)
                    )
                    Spacer(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.space_app_small))
                    )

                    SwitchLabel(
                        stringResource(id = R.string.status_public),
                        editQuizViewModel.isPublic,
                        onChangeValue = {value ->
                            editQuizViewModel.onChangeIsPublic(value)
                        }
                    )

                    Spacer(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.space_app_large))
                    )

                    ButtonComponent(
                        onClick = {
                            editQuizViewModel.editQuiz(id)
                        },
                        "Lưu Thay Đổi",
                        MaterialTheme.colorScheme.primary,
                        editQuiz is ResourceState.Loading,
                        editQuiz !is ResourceState.Loading
                    )

                    Spacer(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.space_app_large))
                    )

                    ListEditQuestionCard(
                        listQuestions,
                        navController,
                        editQuizViewModel,
                        id
                    )

                    Spacer(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.space_app_small))
                    )

                    ButtonNavigate(
                        onClick = {
                            navController.navigate("${Routes.EDIT_QUESTION_SCREEN}/${id}/${-1}")
                        },
                        "Thêm Câu Hỏi",
                        MaterialTheme.colorScheme.secondary,
                    )
                    if (openBottomSheet) {
                        ModalBottomSheet(
                            onDismissRequest = {
                                openBottomSheet = false
                                editQuizViewModel.resetChangeThumbnailState()
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
                                    changeThumbnailState = changeThumbnail,
                                    editQuizViewModel = editQuizViewModel,
                                    quizId = id
                                )
                            },
                            sheetState = sheetState,
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditQuestionCard(question: QuestionDetail, navController: NavController, editQuizViewModel: EditQuizViewModel, index:Int,id:Int){
    val deleteQuestion by editQuizViewModel.deleteQuestion.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        onClick = {
            navController.navigate("${Routes.EDIT_QUESTION_SCREEN}/$id/$index")
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(text = question.title)
            Button(
                onClick = {
                    editQuizViewModel.deleteQuestion(question.id,id,index)
                }
            ) {
                Text(text = "Xóa")
            }
        }
    }
}

@Composable
fun ListEditQuestionCard(list:List<QuestionDetail>?, navController: NavController, editQuizViewModel: EditQuizViewModel,id:Int){
    Text(
        text = "Danh sách câu hỏi (${list?.size ?: 0})"
    )
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        list?.let {question ->
            question.forEachIndexed { index, item ->
                EditQuestionCard(item, navController, editQuizViewModel, index,id)
            }
        }
    }
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
