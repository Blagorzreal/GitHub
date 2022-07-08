package com.example.github.data.local

import android.content.Context
import com.example.github.data.local.repos.IReposDao
import com.example.github.data.local.repos.MockedReposDao
import com.example.github.data.local.repos.ReposDBProvider
import com.example.github.isMocked

class DaoProvider private constructor() {
    companion object {
        fun init(context: Context) {
            ReposDBProvider.init(context)
        }

        val reposDao: IReposDao by lazy {
            if (isMocked)
                MockedReposDao()
            else
                ReposDBProvider.instance.reposDao()
        }
    }
}