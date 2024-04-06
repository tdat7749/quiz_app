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
import com.example.client.datasource.CollectionQuizDataSource
import com.example.client.datasource.MyQuizzesDataSource
import com.example.client.model.Quiz
import com.example.client.repositories.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MyQuizzesViewModel @Inject constructor(
    private val quizRepository: QuizRepository
): ViewModel() {
    var keyword by mutableStateOf("")
        private set

    fun searchOnChange(newValue: String) {
        keyword = newValue
    }

    fun getMyQuizzes(): Flow<PagingData<Quiz>> = Pager(
        PagingConfig(10)
    ){
        MyQuizzesDataSource(quizRepository,keyword)
    }.flow.cachedIn(viewModelScope)
}