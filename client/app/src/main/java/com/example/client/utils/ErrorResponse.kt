package com.example.client.utils

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @field:Json(name="httpStatus") val httpStatus:String?,
    @field:Json(name="httpStatusCode") val httpStatusCode:Int?,
    @field:Json(name="message") val message:String
)