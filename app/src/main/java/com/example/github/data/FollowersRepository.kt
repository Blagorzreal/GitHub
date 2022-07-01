package com.example.github.data

import com.example.github.data.data.UserData
import com.example.github.data.local.DaoProvider
import com.example.github.data.local.user.IUserDao
import com.example.github.data.remote.ApiProvider
import com.example.github.data.remote.ResponseResult
import com.example.github.data.remote.followers.IFollowersApi
import com.example.github.model.UserModel
import com.example.github.util.log.AppLogger

class FollowersRepository(
    private val userData: UserData,
    private val userDao: IUserDao = DaoProvider.userDao,
    private val followersApi: IFollowersApi = ApiProvider.followersApi,
) {
    companion object {
        private const val TAG = "Followers repo"
    }

    val localFollowers = userDao.getFollowers(userData.id)

    suspend fun updateFollowers(): ResponseResult<List<UserModel>> {
        AppLogger.log(TAG, "Update followers")

        val result = followersApi.followers(userData.username)
        if (result is ResponseResult.Success) {
            AppLogger.log(TAG, "Insert remote followers to the db")
            userDao.insertUsersAsFollowers(userData.id, result.model)
        }

        return result
    }
}