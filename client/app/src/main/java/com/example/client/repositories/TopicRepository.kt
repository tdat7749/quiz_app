package com.example.client.repositories

import com.example.client.network.topic.TopicService
import javax.inject.Inject

class TopicRepository @Inject constructor(
    private val topicService: TopicService
) {
    suspend fun getAllTopic() = ApiHelper.safeCallApi {
        topicService.getAllTopic()
    }
}