package com.example.github.data

import com.example.github.data.local.DaoProvider
import com.example.github.data.local.profile.IProfileDao
import com.example.github.data.remote.ApiProvider
import com.example.github.data.remote.ResponseResult
import com.example.github.data.remote.profile.IProfileApi
import com.example.github.util.log.AppLogger

class ProfileRepository(
    private val profileDao: IProfileDao = DaoProvider.profileDao,
    private val profileApi: IProfileApi = ApiProvider.profileApi) {

    companion object {
        private const val TAG = "DashboardRepo"
    }

    val localRepos = profileDao.getAll()

    suspend fun updateRepos(username: String): ResponseResult.ResponseError? {
        AppLogger.log(TAG, "Update repos")

        val result = profileApi.getRepos(username)
        if (result is ResponseResult.Success) {
            AppLogger.log(TAG, "Insert remote data to the db")
            profileDao.deleteAllAndInsertNew(result.data)
        }

        return result as? ResponseResult.ResponseError
    }
}