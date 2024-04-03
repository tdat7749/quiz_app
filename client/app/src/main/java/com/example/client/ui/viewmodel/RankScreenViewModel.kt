package com.example.client.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.client.datasource.HistoryRankRoomDataSource
import com.example.client.datasource.HistoryRankSingleDataSource
import com.example.client.model.HistoryRank
import com.example.client.repositories.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class RankScreenViewModel @Inject constructor(
    private val historyRepository: HistoryRepository
): ViewModel() {


    fun getHistoryRankRoom(roomId:Int): Flow<PagingData<HistoryRank>> = Pager(
        PagingConfig(10)
    ){
        HistoryRankRoomDataSource(historyRepository,roomId)
    }.flow.cachedIn(viewModelScope)

    fun getHistoryRankSingle(quizId:Int): Flow<PagingData<HistoryRank>> = Pager(
        PagingConfig(10)
    ){
        HistoryRankSingleDataSource(historyRepository,quizId)
    }.flow.cachedIn(viewModelScope)
}