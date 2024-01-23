package com.example.client

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class QuizzApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG,"Running on onCreate")
    }
    
    companion object{
        const val TAG = "QuizzApplication"
    }
}