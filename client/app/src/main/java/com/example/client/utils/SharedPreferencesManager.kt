package com.example.client.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.client.model.User
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

object SharedPreferencesManager {
    private const val TOKEN = "TOKEN"
    private lateinit var tokenPreferences: SharedPreferences

    private const val USER = "USER"
    private lateinit var userPreferences: SharedPreferences

    private val gson = Gson()

    fun init(context: Context) {
        tokenPreferences = context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        userPreferences = context.getSharedPreferences(USER, Context.MODE_PRIVATE)
    }

    fun getTokenSharedPreferences(): SharedPreferences {
        if (!::tokenPreferences.isInitialized) {
            throw IllegalStateException("SharedPreferencesManager must be initialized first")
        }
        return tokenPreferences
    }

    fun getUserSharedPreferences(): SharedPreferences {
        if (!::userPreferences.isInitialized) {
            throw IllegalStateException("SharedPreferencesManager must be initialized first")
        }
        return userPreferences
    }

    fun saveUser(user: Any) {
        val json = gson.toJson(user)
        userPreferences.edit().putString(USER,json).apply()
    }

    fun getUser(): User? {
        val json = userPreferences.getString(USER, null)
        return json?.let { gson.fromJson(it, User::class.java) }
    }

    fun getAccessToken():String?{
        return tokenPreferences.getString(AppConstants.ACCESS_TOKEN,null)
    }

    fun saveToken(accessToken:String,refreshToken:String){
        tokenPreferences.edit()
            .putString(AppConstants.ACCESS_TOKEN,accessToken)
            .putString(AppConstants.REFRESH_TOKEN,refreshToken)
            .apply()
    }

    fun clear(){
        tokenPreferences.edit().clear()
            .apply()
        userPreferences.edit().clear()
            .apply()
    }
}