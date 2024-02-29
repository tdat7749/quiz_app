package com.example.client.repositories

import android.util.Log
import com.example.client.utils.ErrorResponse
import com.example.client.utils.ResourceState
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.HttpException

object ApiHelper {
    suspend fun <T> safeCallApi(
        apiCall: suspend () -> T
    ): ResourceState<T> {
        return withContext(Dispatchers.IO){
            try{
                ResourceState.Success(apiCall.invoke())
            }catch(throwable: Throwable){
                when(throwable){
                    is HttpException -> {
                        ResourceState.Error(false, throwable.code(),convertThrowableToErrorResponse(throwable))
                    }
                    else -> {
                        ResourceState.Error(false,500, ErrorResponse("INTERNAL_SERVER",500,"Có lỗi không xác định xảy ra"))
                    }
                }
            }
        }
    }

    fun convertThrowableToErrorResponse(throwable: HttpException): ErrorResponse? {
        return try {
            throwable.response()?.errorBody()?.source()?.let {
                val moshiAdapter = Moshi.Builder().build().adapter(ErrorResponse::class.java)
                moshiAdapter.fromJson(it)
            }
        } catch (exception: Exception) {
            Log.e("Throwable", exception.toString() + exception.message)
            ErrorResponse("INTERNAL_SERVER",500,"Có lỗi không xác định xảy ra")
        }
    }
}