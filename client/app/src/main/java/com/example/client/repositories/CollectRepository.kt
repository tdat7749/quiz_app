package com.example.client.repositories

import com.example.client.network.collect.CollectService
import javax.inject.Inject

class CollectRepository @Inject constructor (
    private val collectService: CollectService
) {
    suspend fun addToCollection(quizId:Int) = ApiHelper.safeCallApi {
        collectService.addToCollection(quizId)
    }

    suspend fun removeFromCollection(quizId:Int) = ApiHelper.safeCallApi {
        collectService.removeFromCollection(quizId)
    }
}