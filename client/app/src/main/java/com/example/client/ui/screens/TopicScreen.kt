package com.example.client.ui.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.client.R
import com.example.client.model.Quiz
import com.example.client.ui.components.*
import com.example.client.ui.viewmodel.SearchViewModel


@SuppressLint("SuspiciousIndentation")
@Composable
fun TopicScreen(
    topicId:Int,
    title:String,
    thumbnail:String,
    navController: NavController,
    searchViewModel:SearchViewModel = hiltViewModel()
) {

    val quizzes = searchViewModel.getQuizzes(topicId).collectAsLazyPagingItems()
    val keyword by searchViewModel.keywordStateFlow.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            ScreenHeader(
                title = title,
                thumbnail = thumbnail,
                navController = navController,
                showBackIcon = true
            )
            Spacer(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.space_app_normal))
            )
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.padding_app)),
            ) {
                TextFieldOutlined(
                    value = keyword,
                    onChangeValue = {
                        searchViewModel.searchOnChange(it)
                    },
                    label = stringResource(id = R.string.search),
                    painterResource = painterResource(id = R.drawable.search)
                )
                Spacer(
                    modifier = Modifier
                        .height(dimensionResource(id = R.dimen.space_app_normal))
                )

                QuizList(quizzes,navController)
            }
        }
    }

@Composable
private fun QuizList(quizzes: LazyPagingItems<Quiz>, navController: NavController){
        LazyColumn (
            contentPadding =  PaddingValues(
                top = dimensionResource(id = R.dimen.space_app_normal),
                bottom = dimensionResource(id = R.dimen.space_app_normal),
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ){
            items(quizzes.itemCount){index ->
                QuizCard(quizzes[index]!!,navController)
            }
            item {
                if(quizzes.loadState.append is LoadState.Loading){
                    LoadingCircle()
                }
            }
        }
}

@Preview
@Composable
fun TopicScreenPreview(){
//    TopicScreen(1)
}