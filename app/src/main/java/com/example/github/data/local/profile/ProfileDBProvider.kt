package com.example.github.data.local.profile

import android.content.Context
import androidx.room.Room
import com.example.github.util.log.AppLogger

class ProfileDBProvider private constructor() {
    companion object {
        private const val TAG = "ProfileDB"
        private const val REPO_TABLE_NAME = "profile"

        lateinit var instance: ProfileDB
        private set

        fun init(context: Context) {
            AppLogger.log(TAG, "Initialized")

            instance = Room.databaseBuilder(
                context,
                ProfileDB::class.java,
                REPO_TABLE_NAME
            ).build()
        }
    }
}