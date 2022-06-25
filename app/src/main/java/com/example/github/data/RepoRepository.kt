package com.example.github.data

import com.example.github.data.data.RepoData
import com.example.github.data.local.DaoProvider
import com.example.github.data.local.repos.IReposDao
import com.example.github.util.log.AppLogger

class RepoRepository(
    private val repoData: RepoData,
    private val reposDao: IReposDao = DaoProvider.reposDao) {

    companion object {
        private const val TAG = "Repo repository"
    }

    suspend fun updateStarred(): Boolean {
        val starred = !repoData.starred
        AppLogger.log(TAG, "Updated repos ${repoData.id} to starred=$starred")

        return try {
            reposDao.update(repoData.id, if (starred) 1 else 0)
            AppLogger.log(TAG, "Updated repos ${repoData.id} with starred=$starred")
            true
        } catch (error: Exception) {
            AppLogger.log(TAG, "Unable to update repos ${repoData.id}: ${error.message}")
            false
        }
    }
}