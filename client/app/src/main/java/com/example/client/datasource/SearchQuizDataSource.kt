package com.example.client.datasource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.client.model.Quiz
import com.example.client.repositories.QuizRepository
import com.example.client.utils.ResourceState
import java.io.IOException
import javax.inject.Inject

class SearchQuizDataSource @Inject constructor(
    private val quizRepository: QuizRepository,
    private val keyword:String,
    private val topicId:Int,
    private val sortBy:String = "createdAt"
): PagingSource<Int, Quiz>() {
    override fun getRefreshKey(state: PagingState<Int, Quiz>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Quiz> {
        return try {
            val page = params.key ?: 0
            val response = quizRepository.getPublicQuiz(keyword,page,sortBy,topicId)
            when(response){
                is ResourceState.Success -> {
                    LoadResult.Page(
                        data = response.value.data.data,
                        prevKey = if (page == 0) null else page.minus(1),
                        nextKey = if (response.value.data.data.isEmpty()) null else page.plus(1),
                    )
                }
                is ResourceState.Error -> {
                    LoadResult.Error(IOException("Có lỗi xảy ra"))
                }
                else -> {
                    LoadResult.Error(IOException("Có lỗi xảy ra"))
                }
            }

        } catch (throwable:Throwable) {
            Log.d("exception",throwable.message.toString())
            LoadResult.Error(throwable)
        }
    }
}