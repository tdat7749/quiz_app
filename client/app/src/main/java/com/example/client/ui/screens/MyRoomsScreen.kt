package com.example.client.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.client.model.Room
import com.example.client.ui.components.*
import com.example.client.ui.components.room.RoomCard
import com.example.client.ui.viewmodel.MyRoomsViewModel

@Composable
fun MyRoomsScreen(
    navController: NavController,
    myRoomsViewModel: MyRoomsViewModel = hiltViewModel()
){
    val rooms: LazyPagingItems<Room> = myRoomsViewModel.getMyRooms().collectAsLazyPagingItems()
    val keyword by myRoomsViewModel.keywordStateFlow.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                "Phòng Chơi Của Tôi",
                navController
            )
        },
        content = {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.padding_app))
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(it),
            ) {
                if(rooms.loadState.refresh is LoadState.Loading){
                    Loading()
                }else if (rooms.loadState.refresh is LoadState.NotLoading){

                        TextFieldOutlined(
                            value = keyword,
                            onChangeValue = {value ->
                                myRoomsViewModel.searchOnChange(value)
                            },
                            label = stringResource(id = R.string.search),
                            painterResource = painterResource(id = R.drawable.search)
                        )
                        Spacer(
                            modifier = Modifier
                                .height(dimensionResource(id = R.dimen.space_app_normal))
                        )

                        RoomList(rooms, navController)
                    }
                }
        }
    )
}

@Composable
private fun RoomList(rooms: LazyPagingItems<Room>, navController: NavController){
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
        items(rooms.itemCount){index ->
            RoomCard(rooms[index]!!,navController,true)
        }
        item {
            if(rooms.loadState.append is LoadState.Loading){
                LoadingCircle()
            }
        }
    }
}