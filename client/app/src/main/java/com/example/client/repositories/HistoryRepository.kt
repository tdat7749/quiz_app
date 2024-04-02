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
}