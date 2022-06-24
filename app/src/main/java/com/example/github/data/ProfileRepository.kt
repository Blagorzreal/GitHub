package com.example.github.data

import com.example.github.data.data.UserData
import com.example.github.data.local.DaoProvider
import com.example.github.data.local.profile.IProfileDao
import com.example.github.data.remote.ApiProvider
import com.example.github.data.remote.ResponseResult
import com.example.github.data.remote.profile.IProfileApi
import com.example.github.model.RepoModel
import com.example.github.util.log.AppLogger

class ProfileRepository(
    private val owner: UserData,
    private val profileDao: IProfileDao = DaoProvider.profileDao,
    private val profileApi: IProfileApi = ApiProvider.profileApi) {

    companion object {
        private const val TAG = "ProfileRepo"
    }

    val localRepos = profileDao.getAll(owner.id)

    suspend fun updateRepos(username: String): ResponseResult<List<RepoModel>> {
        AppLogger.log(TAG, "Update repos")

        val result = profileApi.getRepos(username)
        if (result is ResponseResult.Success) {
            AppLogger.log(TAG, "Insert remote repos to the db")
            profileDao.deleteAllAndInsertNew(result.model, owner.id)
        }

        return result
    }
}