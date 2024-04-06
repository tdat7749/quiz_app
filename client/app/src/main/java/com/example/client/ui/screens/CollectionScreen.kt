package com.example.client.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.client.R
import com.example.client.model.Quiz
import com.example.client.ui.components.*
import com.example.client.ui.viewmodel.CollectViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CollectionScreen(
    navController: NavController,
    collectViewModel: CollectViewModel = hiltViewModel()
){

    val collection: LazyPagingItems<Quiz> = collectViewModel.getCollection().collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopBar(
                "Bộ Sưu Tập",
                navController
            )
        },
        content = {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.padding_app))
                    .padding(it),
            ) {
                    if(collection.loadState.refresh is LoadState.Loading){
                        Loading()
                    }else if (collection.loadState.refresh is LoadState.NotLoading){
                        TextFieldOutlined(
                            value = collectViewModel.keyword,
                            onChangeValue = {
                                collectViewModel.searchOnChange(it)
                            },
                            label = stringResource(id = R.string.search),
                            painterResource = painterResource(id = R.drawable.search)
                        )
                        Spacer(
                            modifier = Modifier
                                .height(dimensionResource(id = R.dimen.space_app_normal))
                        )

                        QuizList(collection, navController)
                    }
                }
        }
    )
}

@Composable
private fun QuizList(collection:LazyPagingItems<Quiz>, navController: NavController){
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
        items(collection.itemCount){index ->
            QuizCardUser(collection[index]!!,navController)
        }
        item {
            if(collection.loadState.append is LoadState.Loading){
                LoadingCircle()
            }
        }
    }
}