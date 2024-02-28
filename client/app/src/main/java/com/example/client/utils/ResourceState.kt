package com.example.client.utils

import retrofit2.Response

sealed class ResourceState<out T> {
    object Loading : ResourceState<Nothing>()
    data class Success<out T> (val value:T?) : ResourceState<T>()
    data class Error(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ErrorResponse?
    ) : ResourceState<Nothing>()
}