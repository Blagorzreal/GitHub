package com.example.github.data.local

import com.example.github.data.local.repos.IReposDao
import com.example.github.data.local.repos.MockedReposDao
import com.example.github.isMocked

class DaoProvider private constructor() {
    companion object {
        val reposDao: IReposDao by lazy {
            if (isMocked)
                MockedReposDao()
            else
                MockedReposDao()
        }
    }
}