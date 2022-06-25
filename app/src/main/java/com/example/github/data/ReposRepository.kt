package com.example.github.data

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
        private const val TAG = "Repos repository"
    }

    val localRepos = reposDao.getAll(owner.id)

    val starredRepos = reposDao.getAllStarred()

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