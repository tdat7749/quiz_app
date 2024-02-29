package com.example.client.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.client.model.Quiz
import com.example.client.repositories.ApiHelper
import com.example.client.repositories.QuizRepository
import com.example.client.utils.ApiResponse
import com.example.client.utils.PagingResponse
import com.example.client.utils.ResourceState
import com.example.client.utils.Search
import java.io.IOException
import javax.inject.Inject

class SearchQuizDataSource @Inject constructor(
    private val quizRepository: QuizRepository,
    private val search:Search,
    private val topicId:Int
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
            val response = quizRepository.getPublicQuiz(search.keyword,page,search.sortBy,topicId)

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
            LoadResult.Error(throwable)
        }
    }
}