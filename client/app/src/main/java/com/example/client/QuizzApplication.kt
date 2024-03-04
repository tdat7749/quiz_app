package com.example.client

import android.app.Application
import android.util.Log
import com.example.client.utils.SharedPreferencesManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class QuizzApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManager.init(this)
        Log.d(TAG,"Running on onCreate")
    }
    
    companion object{
        const val TAG = "QuizzApplication"
    }
}