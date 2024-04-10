package com.example.client.datasource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.client.model.Quiz
import com.example.client.model.Room
import com.example.client.repositories.QuizRepository
import com.example.client.repositories.RoomRepository
import com.example.client.utils.ResourceState
import java.io.IOException
import javax.inject.Inject

class RoomJoinedDataSource @Inject constructor(
    private val roomRepository: RoomRepository,
): PagingSource<Int, Room>() {
    override fun getRefreshKey(state: PagingState<Int, Room>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Room> {
        return try {
            val page = params.key ?: 0
            val response = roomRepository.getJoinedRoom(page)
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