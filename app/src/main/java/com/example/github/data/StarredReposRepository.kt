package com.example.github.data

import com.example.github.data.data.UserData

class StarredReposRepository(userData: UserData): ReposRepository(TAG, userData) {

    companion object {
        private const val TAG = "Starred repos repo"
    }

    val starredRepos = reposDao.getAllStarred()
}