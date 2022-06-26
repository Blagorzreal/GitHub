package com.example.github.data

import com.example.github.data.data.UserData
import com.example.github.data.local.DaoProvider
import com.example.github.data.local.repos.IReposDao
import com.example.github.data.remote.ApiProvider
import com.example.github.data.remote.ResponseResult
import com.example.github.data.remote.repos.IReposApi
import com.example.github.model.RepoModel
import com.example.github.util.log.AppLogger

open class UserReposRepository(
    protected val tag: String = TAG,
    protected val owner: UserData,
    protected val reposDao: IReposDao = DaoProvider.reposDao,
    protected val reposApi: IReposApi = ApiProvider.reposApi
) {
    val localRepos = reposDao.getAll(owner.id)

    companion object {
        private const val TAG = "User repos repo"
    }

    suspend fun updateRepos(username: String): ResponseResult<List<RepoModel>> {
        AppLogger.log(tag, "Update repos")

        val result = reposApi.getRepos(username)
        if (result is ResponseResult.Success) {
            AppLogger.log(tag, "Insert remote repos to the db")
            reposDao.deleteAllAndInsertNew(result.model, owner.id)
        }

        return result
    }
}