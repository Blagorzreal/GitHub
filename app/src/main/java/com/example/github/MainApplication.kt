package com.example.github

import android.app.Application
import com.example.github.data.LoginSession

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        LoginSession.init(this)
    }
}