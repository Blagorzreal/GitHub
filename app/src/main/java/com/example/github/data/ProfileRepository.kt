package com.example.github.data

import com.example.github.data.data.UserData

class ProfileRepository(userData: UserData): ReposRepository(TAG, userData) {

    companion object {
        private const val TAG = "Profile repo"
    }

    val starredRepos = reposDao.getAllStarred()
}