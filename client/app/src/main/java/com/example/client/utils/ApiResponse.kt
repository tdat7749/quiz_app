package com.example.client.utils

data class ApiResponse<T>(
    val message:String,
    val data:T
)