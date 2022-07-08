package com.example.github.data

import com.example.github.data.data.UserData
import com.example.github.data.local.user.IUserDao
import com.example.github.data.remote.ResponseResult
import com.example.github.data.remote.followers.IFollowersApi
import com.example.github.model.UserModel
import com.example.github.util.log.AppLogger
import dagger.Module
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Inject

@Module
@InstallIn(ViewModelComponent::class)
class FollowersRepository @AssistedInject constructor(@Assisted val userData: UserData) {

    companion object {
        private const val TAG = "Followers repo"
    }

    @AssistedFactory
    interface FollowersRepositoryModuleFactory {
        fun create(userData: UserData): FollowersRepository
    }

    @Inject lateinit var userDao: IUserDao
    @Inject lateinit var followersApi: IFollowersApi

    val localFollowers by lazy { userDao.getFollowers(userData.id) }

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