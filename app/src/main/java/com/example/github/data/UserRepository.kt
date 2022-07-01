package com.example.github.data

import com.example.github.data.data.UserData
import com.example.github.data.local.DaoProvider
import com.example.github.data.local.user.IUserDao
import com.example.github.data.remote.ApiProvider
import com.example.github.data.remote.ResponseResult
import com.example.github.data.remote.user.IUserApi
import com.example.github.model.UserModel
import com.example.github.util.log.AppLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserRepository(
    private val userData: UserData,
    protected val userDao: IUserDao = DaoProvider.userDao,
    protected val userApi: IUserApi = ApiProvider.userApi,
) {
    companion object {
        private const val TAG = "User repo"
    }

    private var _localUser = MutableStateFlow<UserModel?>(null)
    val localUser: StateFlow<UserModel?> = _localUser

    suspend fun updateUser(): ResponseResult<UserModel> {
        AppLogger.log(TAG, "Update user")

        _localUser.value = userDao.getById(userData.id)

        val result = userApi.getUser(_localUser.value?.login ?: userData.username)
        if (result is ResponseResult.Success) {
            AppLogger.log(TAG, "Insert remote user to the db")
            userDao.insertUsers(listOf(result.model))
            _localUser.value = result.model
        }

        return result
    }
}