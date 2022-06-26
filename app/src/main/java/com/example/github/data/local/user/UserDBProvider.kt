package com.example.github.data.local.user

import android.content.Context
import androidx.room.Room
import com.example.github.util.log.AppLogger

class UserDBProvider private constructor() {
    companion object {
        private const val TAG = "UserDB"
        private const val REPO_TABLE_NAME = "user"

        lateinit var instance: UserDB
        private set

        fun init(context: Context) {
            AppLogger.log(TAG, "Initialized")

            instance = Room.databaseBuilder(
                context,
                UserDB::class.java,
                REPO_TABLE_NAME
            ).build()
        }
    }
}