package com.example.client.network.topic

import com.example.client.model.Topic
import com.example.client.utils.ApiResponse
import retrofit2.http.GET

interface TopicService {
    @GET("api/topics/")
    suspend fun getAllTopic() : ApiResponse<List<Topic>>
}