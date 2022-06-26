package com.example.github.data

import com.example.github.data.data.RepoData
import com.example.github.data.local.DaoProvider
import com.example.github.data.local.repos.IReposDao
import com.example.github.util.log.AppLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RepoRepository(
    repoData: RepoData,
    private val reposDao: IReposDao = DaoProvider.reposDao) {

    companion object {
        private const val TAG = "Repo repo"
    }

    private val _repoData = MutableStateFlow(repoData)
    val repoData: StateFlow<RepoData> = _repoData

    suspend fun updateStarred(): Boolean {
        val repoDataValue = _repoData.value
        val repoDataId = repoDataValue.id
        val starred = !repoDataValue.starred

        AppLogger.log(TAG, "Update repo $repoDataId to starred=$starred")

        return try {
            reposDao.update(repoDataId, if (starred) 1 else 0)

            _repoData.value = repoDataValue.copy(starred = starred)
            AppLogger.log(TAG, "Updated repo $repoDataId")

            true
        } catch (error: Exception) {
            AppLogger.log(TAG, "Unable to update repo $repoDataId: ${error.message}")
            false
        }
    }
}