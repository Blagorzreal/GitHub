package com.example.github.data

import com.example.github.data.data.RepoData
import com.example.github.data.data.UserData
import com.example.github.data.local.DaoProvider
import com.example.github.data.local.repos.IReposDao
import com.example.github.data.remote.ApiProvider
import com.example.github.data.remote.ResponseResult
import com.example.github.data.remote.repos.IReposApi
import com.example.github.model.RepoModel
import com.example.github.util.log.AppLogger

class ReposRepository(
    private val owner: UserData,
    private val reposDao: IReposDao = DaoProvider.reposDao,
    private val reposApi: IReposApi = ApiProvider.reposApi) {

    companion object {
        private const val TAG = "ReposRepo"
    }

    val localRepos = reposDao.getAll(owner.id)

    val starredRepos = reposDao.getAllStarred()

    suspend fun updateStarred(repoData: RepoData, starred: Boolean): Boolean {
        return try {
            reposDao.update(repoData.id, if (starred) 1 else 0)
            AppLogger.log(TAG, "Updated repos ${repoData.id} with starred=$starred")
            true
        } catch (error: Exception) {
            AppLogger.log(TAG, "Unable to update repos ${repoData.id}: ${error.message}")
            false
        }
    }

    suspend fun updateRepos(username: String): ResponseResult<List<RepoModel>> {
        AppLogger.log(TAG, "Update repos")

        val result = reposApi.getRepos(username)
        if (result is ResponseResult.Success) {
            AppLogger.log(TAG, "Insert remote repos to the db")
            reposDao.deleteAllAndInsertNew(result.model, owner.id)
        }

        return result
    }
}