package com.example.client.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.client.datasource.MyQuizzesDataSource
import com.example.client.datasource.MyRoomsDataSource
import com.example.client.model.Quiz
import com.example.client.model.Room
import com.example.client.repositories.QuizRepository
import com.example.client.repositories.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@HiltViewModel
class MyRoomsViewModel @Inject constructor(
    private val roomRepository: RoomRepository
): ViewModel() {
    private val _keywordStateFlow = MutableStateFlow("")
    val keywordStateFlow = _keywordStateFlow.asStateFlow()

    fun searchOnChange(newValue: String) {
        _keywordStateFlow.value = newValue
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun getMyRooms(): Flow<PagingData<Room>> = _keywordStateFlow
        .debounce(700)
        .distinctUntilChanged()
        .flatMapLatest {keyword ->
            Pager(PagingConfig(pageSize = 10,prefetchDistance = 1)){
                MyRoomsDataSource(roomRepository,keyword)
            }.flow
        }.cachedIn(viewModelScope)
}