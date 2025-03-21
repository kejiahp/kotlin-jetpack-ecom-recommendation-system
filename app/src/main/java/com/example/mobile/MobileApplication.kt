package com.example.mobile

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

/**
 * Using hilt, it is required every project has on application class.
 *
 * It is executed when ever the application is launched
 *
 * `HiltAndroidApp` annotation allow the application class knows this will be a hilt project
 */
@HiltAndroidApp
class MobileApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Application_class_onCreate()_hit")
    }

    companion object {
        const val TAG =  "MobileApplication"
    }
}