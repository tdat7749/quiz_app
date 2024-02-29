package com.example.client.utils

data class ApiResponse<T>(
    val message:String,
    val data:T
)

data class PagingResponse<T>(
    val totalRecord: Int,
    val totalPage:Int,
    val data:T
)

data class Search(
    val keyword:String,
    val sortBy:String = "createdAt",
    val pageIndex:Int?,
)