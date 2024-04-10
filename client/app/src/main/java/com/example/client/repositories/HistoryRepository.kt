package com.example.client.repositories

import com.example.client.model.CreateHistory
import com.example.client.network.history.HistoryService
import javax.inject.Inject

class HistoryRepository @Inject constructor(
    private val historyService:HistoryService
) {
    suspend fun createHistory(data:CreateHistory) = ApiHelper.safeCallApi {
        historyService.createHistory(data)
    }

    suspend fun getHistoryRankRoom(roomId:Int,pageIndex:Int) = ApiHelper.safeCallApi {
        historyService.getHistoryRankRoom(roomId,pageIndex)
    }

    suspend fun getHistoryRankSingle(quizId:Int,pageIndex: Int) = ApiHelper.safeCallApi {
        historyService.getHistoryRankSingle(quizId,pageIndex)
    }

    suspend fun getHistoryAnswer(roomId:Int) = ApiHelper.safeCallApi {
        historyService.getHistoryAnswer(roomId)
    }
}