package com.example.github.data.local

import android.content.Context
import com.example.github.data.local.profile.IProfileDao
import com.example.github.data.local.profile.ProfileDBProvider

class DaoProvider private constructor() {
    companion object {
        fun init(context: Context) {
            ProfileDBProvider.init(context)
        }

        val profileDao: IProfileDao by lazy { ProfileDBProvider.instance.profileDao() }

        suspend fun clean() {
            profileDao.deleteAll()
        }
    }
}