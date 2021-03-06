package com.example.github

import android.app.Application
import com.example.github.data.LoginSession
import com.example.github.util.NetworkManager
import com.example.github.util.log.AppLogger
import com.example.github.util.log.LogType
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication: Application() {
    companion object {
        private const val TAG = "App"
    }

    override fun onCreate() {
        super.onCreate()

        NetworkManager.init(this)
        LoginSession.init(this)
    }

    override fun onTerminate() {
        AppLogger.log(TAG, "Terminate")
        super.onTerminate()
    }

    override fun onLowMemory() {
        AppLogger.log(TAG, "Low memory", LogType.Warning)
        super.onLowMemory()
    }
}