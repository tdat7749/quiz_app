package com.example.client.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesManager {
    private const val PREFS_NAME = "QuizSharedPreferences"
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getSharedPreferences(): SharedPreferences {
        if (!::sharedPreferences.isInitialized) {
            throw IllegalStateException("SharedPreferencesManager must be initialized first")
        }
        return sharedPreferences
    }
}