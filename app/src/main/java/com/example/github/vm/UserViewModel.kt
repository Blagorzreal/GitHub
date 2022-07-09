package com.example.github.vm

import androidx.lifecycle.SavedStateHandle
import com.example.github.data.UserRepository
import com.example.github.data.data.UserData
import com.example.github.model.UserModel
import com.example.github.ui.navigation.Route.Companion.USER_DATA
import com.example.github.util.log.AppLogger
import com.example.github.util.mapper.UserModelMapper
import com.example.github.vm.base.BaseApiViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    userRepositoryFactory: UserRepository.UserRepositoryModuleFactory
    ): BaseApiViewModel<UserData, UserModel>(UserModelMapper::userModelToUserData) {

    override val tag = "User VM"

    private val userRepository: UserRepository by lazy {
        userRepositoryFactory.create(savedStateHandle.get<UserData>(USER_DATA) ?: throw Exception())
    }

    val userData by lazy { userRepository.userData }

    val followers: StateFlow<Long?> = userRepository.followers
    val following: StateFlow<Long?> = userRepository.following

    init {
        updateUser()
    }

    fun updateUser() {
        AppLogger.log(tag, "Update user")
        fetchData { userRepository.updateUser() }
    }
}