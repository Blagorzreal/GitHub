package com.example.github.util.log

import android.util.Log

sealed class AppLogger private constructor() {
    companion object {
        private const val TAG = "AppLogger"

        fun log(tag: String? = TAG, message: String, logType: LogType = LogType.Info) {
            when (logType) {
                LogType.Info -> Log.v(tag, message)
                LogType.Warning -> Log.w(tag, message)
                LogType.Error -> Log.e(tag, message)
            }
        }
    }
}