package com.example.github.data

import com.example.github.data.data.UserData
import com.example.github.data.local.DaoProvider
import com.example.github.data.local.user.IUserDao
import com.example.github.data.remote.ApiProvider
import com.example.github.data.remote.ResponseResult
import com.example.github.data.remote.user.IUserApi
import com.example.github.model.UserModel
import com.example.github.util.log.AppLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class UserRepository(
    private val user: UserData,
    protected val userDao: IUserDao = DaoProvider.userDao,
    protected val userApi: IUserApi = ApiProvider.userApi,
) {
    companion object {
        private const val TAG = "User repo"
    }

    private var _localUser: MutableStateFlow<UserModel?> = MutableStateFlow(null)
    var localUser: Flow<UserModel?> = _localUser

    suspend fun updateUser(): ResponseResult<UserModel> {
        AppLogger.log(TAG, "Update user")

        _localUser.value = userDao.getById(user.id)

        val result = userApi.getUser(_localUser.value?.login ?: user.username)
        if (result is ResponseResult.Success) {
            AppLogger.log(TAG, "Insert remote user to the db")
            userDao.insertUser(result.model)
        }

        return result
    }
}