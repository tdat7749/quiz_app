package com.example.client.adapter

import android.os.Build
import androidx.annotation.RequiresApi
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DateJsonAdapter {
    @RequiresApi(Build.VERSION_CODES.O)
    private val dateFormat = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @RequiresApi(Build.VERSION_CODES.O)
    @ToJson
    fun toJson(date: LocalDateTime): String {
        return dateFormat.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @FromJson
    fun fromJson(dateString: String): LocalDateTime? {
        return  LocalDateTime.parse(dateString, dateFormat)
    }
}