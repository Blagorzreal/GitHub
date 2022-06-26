package com.example.github.data

import com.example.github.data.data.UserData

class ReposRepository(owner: UserData): UserReposRepository(TAG, owner) {

    companion object {
        private const val TAG = "Repos repository"
    }

    val starredRepos = reposDao.getAllStarred()
}