package com.example.client.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.client.datasource.MyRoomsDataSource
import com.example.client.datasource.SearchQuizDataSource
import com.example.client.model.Quiz
import com.example.client.repositories.QuizRepository
import com.example.client.utils.Search
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val quizRepository: QuizRepository
): ViewModel(){
    private val _keywordStateFlow = MutableStateFlow("")
    val keywordStateFlow = _keywordStateFlow.asStateFlow()

    fun searchOnChange(newValue: String) {
        _keywordStateFlow.value = newValue
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun getQuizzes(topicId:Int): Flow<PagingData<Quiz>> = _keywordStateFlow
        .debounce(700)
        .distinctUntilChanged()
        .flatMapLatest {keyword ->
            Pager(PagingConfig(pageSize = 10,prefetchDistance = 1)){
                SearchQuizDataSource(quizRepository,keyword,topicId)
            }.flow
        }.cachedIn(viewModelScope)
}