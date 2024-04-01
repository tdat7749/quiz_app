package com.example.client.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.client.ui.viewmodel.CreateQuizViewModel
import com.example.client.R
import com.example.client.model.AuthToken
import com.example.client.model.CreateQuestion
import com.example.client.model.Quiz
import com.example.client.model.Topic
import com.example.client.ui.components.*
import com.example.client.ui.components.TextFieldOutlined
import com.example.client.ui.navigation.Routes
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import com.example.client.utils.Utilities

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuizScreen (
    navController: NavController,
    createQuizViewModel: CreateQuizViewModel
){

    val context = LocalContext.current
    val listQuestion by createQuizViewModel.listQuestion.collectAsState()

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            createQuizViewModel.onChangeImageUrl(uri)
            val path = Utilities.Companion.getRealPathFromURI(uri!!,context)
            if(path != null){
                createQuizViewModel.onChangeQuizThumbnailPath(path)
            }
        }

    val topics by createQuizViewModel.topics.collectAsState()
    val quiz by createQuizViewModel.quiz.collectAsState()

    when(quiz){
        is ResourceState.Success -> {
            ShowMessage((quiz as ResourceState.Success<ApiResponse<Quiz>>).value.message)
        }
        is ResourceState.Error -> {
            (quiz as ResourceState.Error).errorBody?.let { ShowMessage(it.message) { createQuizViewModel.resetQuizState() } }
        }
        else ->{

        }
    }

    Scaffold(
        topBar = {
            TopBar(
                stringResource(id = R.string.create_quiz),
                navController
            )
        },
        content = {
            Surface (
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .verticalScroll(rememberScrollState())
                    .padding(it)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(id = R.dimen.padding_app))

                ) {
                    when(topics){
                        is ResourceState.Loading -> {
                            Loading()
                        }
                        is ResourceState.Success -> {

                            ImageCard(createQuizViewModel.imageUri.value)

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
                                createQuizViewModel.topic,
                                onChangeValue = {topic ->
                                    createQuizViewModel.onChangeTopic(topic)
                                }
                            )

                            TextFieldOutlined(
                                createQuizViewModel.title,
                                onChangeValue = {value ->
                                    createQuizViewModel.onChangeTitle(value)
                                },
                                "Tên Quiz",
                                painterResource(id = R.drawable.title)
                            )
                            Spacer(
                                modifier = Modifier
                                    .height(dimensionResource(id = R.dimen.space_app_small))
                            )

                            TextFieldOutlined(
                                createQuizViewModel.summary,
                                onChangeValue = {value ->
                                    createQuizViewModel.onChangeSummary(value)
                                },
                                "Giới Thiệu Ngắn",
                                painterResource(id = R.drawable.summary)
                            )
                            Spacer(
                                modifier = Modifier
                                    .height(dimensionResource(id = R.dimen.space_app_small))
                            )

                            TextFieldOutlined(
                                createQuizViewModel.description,
                                onChangeValue = {value ->
                                    createQuizViewModel.onChangeDescription(value)
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
                                createQuizViewModel.isPublic,
                                onChangeValue = {value ->
                                    createQuizViewModel.onChangeIsPublic(value)
                                }
                            )
                            Spacer(
                                modifier = Modifier
                                    .height(dimensionResource(id = R.dimen.space_app_small))
                            )

                            ListCreateQuestionCard(
                                listQuestion,
                                navController,
                                createQuizViewModel
                            )

                            Spacer(
                                modifier = Modifier
                                    .height(dimensionResource(id = R.dimen.space_app_large))
                            )

                            ButtonNavigate(
                                onClick = {
                                    navController.navigate("${Routes.QUESTION_SCREEN}/${-1}")
                                },
                                "Thêm Câu Hỏi",
                                MaterialTheme.colorScheme.secondary,
                            )

                            Spacer(
                                modifier = Modifier
                                    .height(dimensionResource(id = R.dimen.space_app_small))
                            )

                            ButtonComponent(
                                onClick = {
                                    createQuizViewModel.createQuiz()
                                },
                                "Tạo Quiz",
                                MaterialTheme.colorScheme.primary,
                                quiz is ResourceState.Loading,
                                quiz !is ResourceState.Loading
                            )
                        }
                        else -> {

                        }
                    }
                }
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuestionCard(question:CreateQuestion,navController: NavController,createQuizViewModel: CreateQuizViewModel,index:Int){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        onClick = {
            navController.navigate("${Routes.QUESTION_SCREEN}/$index")
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
                    createQuizViewModel.deleteQuestion(index)
                }
            ) {
                Text(text = "Xóa")
            }
        }
    }
}

@Composable
fun ListCreateQuestionCard(list:List<CreateQuestion>?,navController: NavController,createQuizViewModel: CreateQuizViewModel){
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
                CreateQuestionCard(item, navController, createQuizViewModel, index)
            }
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
