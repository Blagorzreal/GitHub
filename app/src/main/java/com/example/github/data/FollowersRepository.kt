package com.example.github.data

import com.example.github.data.data.UserData
import com.example.github.data.local.user.IUserDao
import com.example.github.data.remote.ResponseResult
import com.example.github.data.remote.followers.IFollowersApi
import com.example.github.model.UserModel
import com.example.github.util.log.AppLogger
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class FollowersRepository @AssistedInject constructor(
    @Assisted val userData: UserData,
    private val userDao: IUserDao,
    private val followersApi: IFollowersApi) {

    companion object {
        private const val TAG = "Followers repo"
    }

    @AssistedFactory
    interface FollowersRepositoryModuleFactory {
        fun create(userData: UserData): FollowersRepository
    }

    val localFollowers = userDao.getFollowers(userData.id)

    suspend fun updateFollowers(): ResponseResult<List<UserModel>> {
        AppLogger.log(TAG, "Update followers")

        val result = followersApi.getFollowers(userData.username)
        if (result is ResponseResult.Success) {
            AppLogger.log(TAG, "Insert remote followers to the db")
            userDao.insertUsersAsFollowers(userData.id, result.model)
        }

        return result
    }
}