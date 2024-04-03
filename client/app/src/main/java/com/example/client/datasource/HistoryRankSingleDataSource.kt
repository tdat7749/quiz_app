package com.example.client.datasource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.client.model.HistoryRank
import com.example.client.repositories.HistoryRepository
import com.example.client.utils.ResourceState
import java.io.IOException
import javax.inject.Inject

class HistoryRankSingleDataSource @Inject constructor(
    private val historyRepository: HistoryRepository,
    private val quizId:Int
): PagingSource<Int, HistoryRank>() {
    override fun getRefreshKey(state: PagingState<Int, HistoryRank>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HistoryRank> {
        return try {
            val page = params.key ?: 0
            when(val response = historyRepository.getHistoryRankSingle(quizId,page)){
                is ResourceState.Success -> {
                    val value = response.value.data.data as List<HistoryRank>
                    LoadResult.Page(
                        data = value as List<HistoryRank>,
                        prevKey = if (page == 0) null else page.minus(1),
                        nextKey = if (value.isEmpty()) null else page.plus(1),
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