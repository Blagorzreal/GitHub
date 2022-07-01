package com.example.github.vm

import com.example.github.data.UserRepository
import com.example.github.data.data.UserData
import com.example.github.model.UserModel
import com.example.github.util.log.AppLogger
import com.example.github.util.mapper.UserModelMapper
import com.example.github.vm.base.BaseApiViewModel
import kotlinx.coroutines.flow.StateFlow

class UserViewModel(
    val userData: UserData,
    private val userRepository: UserRepository = UserRepository(userData)
): BaseApiViewModel<UserData, UserModel>(TAG, UserModelMapper::userModelToUserData) {
    companion object {
        private const val TAG = "User VM"
    }

    val followers: StateFlow<Long?> = userRepository.followers
    val following: StateFlow<Long?> = userRepository.following

    init {
        updateUser()
    }

    fun updateUser() {
        AppLogger.log(tag, "Get user")
        fetchData { userRepository.updateUser() }
    }
}