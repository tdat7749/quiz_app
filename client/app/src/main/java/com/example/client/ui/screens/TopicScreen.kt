package com.example.client.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.client.R
import com.example.client.ui.components.QuizCardScroll
import com.example.client.ui.components.TextFieldOutlined
import com.example.client.ui.viewmodel.SearchViewModel


@Composable
fun TopicScreen(
    searchViewModel:SearchViewModel = hiltViewModel()
) {

    Surface (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_app)),
            verticalArrangement = Arrangement.Top
        ) {
            TopicHeader("https://www.proprofs.com/quiz-school/topic_images/p191f89lnh17hs1qnk9fj1sm113b3.jpg","Lịch Sử")
            Spacer(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.space_app_normal))
            )

            TextFieldOutlined(
                value = searchViewModel.keyword,
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

            QuizList(searchViewModel)
        }
    }
}

@Composable
fun TopicHeader(thubnail:String, title:String){
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.search_header)),
        contentAlignment = Alignment.BottomCenter
    ){
        AsyncImage(
            model = thubnail,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.search_header))
        )

        Text(
            text = title,
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(bottom = 14.dp),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun QuizList(searchViewModel:SearchViewModel){
    if(!searchViewModel.isSearchKeywordBlank()){
        val quizzes = searchViewModel.getQuizzes().collectAsLazyPagingItems()
        LazyColumn (
            contentPadding =  PaddingValues(
                top = dimensionResource(id = R.dimen.space_app_normal),
                bottom = dimensionResource(id = R.dimen.space_app_normal)
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            items(quizzes.itemCount){index ->
                QuizCardScroll(quizzes[index]!!)
            }
        }
    }
}

@Preview
@Composable
fun TopicScreenPreview(){
    TopicScreen()
}