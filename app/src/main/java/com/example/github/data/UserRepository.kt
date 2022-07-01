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

    private val _followers: MutableStateFlow<Long?> = MutableStateFlow(null)
    val followers: StateFlow<Long?> = _followers

    private val _following: MutableStateFlow<Long?> = MutableStateFlow(null)
    val following: StateFlow<Long?> = _following

    suspend fun updateUser(): ResponseResult<UserModel> {
        AppLogger.log(TAG, "Update user")

        userDao.getById(userData.id)?.let {
            updateWithNewData(it.followers, it.following)
        }

        val result = userApi.getUser(userData.username)
        if (result is ResponseResult.Success) {
            AppLogger.log(TAG, "Insert remote user to the db")

            result.model.let {
                updateWithNewData(it.followers, it.following)
            }

            userDao.insertUsers(listOf(result.model))
        }

        return result
    }

    private fun updateWithNewData(followers: Long?, following: Long?) {
        userData.followers = followers
        userData.following = following

        _followers.value = followers
        _following.value = following
    }
}