package com.example.github.data.local.repos

import android.content.Context
import androidx.room.Room
import com.example.github.util.log.AppLogger

class ReposDBProvider private constructor() {
    companion object {
        private const val TAG = "ReposDB"
        private const val REPO_TABLE_NAME = "repos"

        lateinit var instance: ReposDB
        private set

        fun init(context: Context) {
            AppLogger.log(TAG, "Initialized")

            instance = Room.databaseBuilder(
                context,
                ReposDB::class.java,
                REPO_TABLE_NAME
            ).build()
        }
    }
}