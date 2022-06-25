package com.example.github.data.local

import android.content.Context
import com.example.github.data.local.repos.IReposDao
import com.example.github.data.local.repos.ReposDBProvider

class DaoProvider private constructor() {
    companion object {
        fun init(context: Context) {
            ReposDBProvider.init(context)
        }

        val reposDao: IReposDao by lazy { ReposDBProvider.instance.reposDao() }

        suspend fun clean() {
            reposDao.deleteAll()
        }
    }
}