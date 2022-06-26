package com.example.github.data

import com.example.github.data.data.UserData
import com.example.github.data.local.DaoProvider
import com.example.github.data.local.user.IUserDao
import com.example.github.data.remote.ApiProvider
import com.example.github.data.remote.ResponseResult
import com.example.github.data.remote.user.IUserApi
import com.example.github.model.UserModel
import com.example.github.util.log.AppLogger
import kotlinx.coroutines.flow.firstOrNull

class UserRepository(
    private val user: UserData,
    protected val userDao: IUserDao = DaoProvider.userDao,
    protected val userApi: IUserApi = ApiProvider.userApi,
) {
    companion object {
        private const val TAG = "User repo"
    }

    val localUser = userDao.getById(user.id)

    suspend fun updateUser(): ResponseResult<UserModel> {
        AppLogger.log(TAG, "Update user")

        val result = userApi.getUser(localUser.firstOrNull()?.login ?: user.username)
        if (result is ResponseResult.Success) {
            AppLogger.log(TAG, "Insert remote user to the db")
            userDao.insertUser(result.model)
        }

        return result
    }
}