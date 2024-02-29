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
import com.example.client.datasource.SearchQuizDataSource
import com.example.client.model.Quiz
import com.example.client.repositories.QuizRepository
import com.example.client.utils.Search
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val quizRepository: QuizRepository
): ViewModel(){

    var keyword by mutableStateOf("")
        private set

    fun isSearchKeywordBlank(): Boolean = keyword.isBlank()
    fun searchOnChange(newValue: String) { keyword = newValue }

    val search = Search(keyword, pageIndex = null)

    fun getQuizzes(): Flow<PagingData<Quiz>> = Pager(
        PagingConfig(10)
    ){
        SearchQuizDataSource(quizRepository,search,1)
    }.flow.cachedIn(viewModelScope)
}