package com.example.github.data

import android.content.Context
import com.example.github.data.data.UserData
import com.example.github.data.preference.LoginPreferences
import com.example.github.util.log.AppLogger

class LoginSession {
    companion object {
        private const val TAG = "LoginSession"
        private const val LOGIN_SHARED_PREFERENCES = "LOGIN_SHARED_PREFERENCES"

        private lateinit var loginPreferences: LoginPreferences

        fun init(context: Context) {
            AppLogger.log(TAG, "Initialized")
            loginPreferences = LoginPreferences(context, LOGIN_SHARED_PREFERENCES)
        }

        val userData get() = loginPreferences.userData

        fun setUserData(userData: UserData?) {
            loginPreferences.setUserData(userData)
        }

        val isActive get() = !userData?.username.isNullOrBlank()

        fun clean() = loginPreferences.clean()
    }
}