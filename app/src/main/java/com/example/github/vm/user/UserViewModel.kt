package com.example.github.vm.user

import androidx.lifecycle.viewModelScope
import com.example.github.data.UserRepository
import com.example.github.data.data.UserData
import com.example.github.model.UserModel
import com.example.github.util.LoginHelper
import com.example.github.util.log.AppLogger
import com.example.github.vm.BaseApiViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserViewModel(
    userData: UserData,
    private val userRepository: UserRepository = UserRepository(userData)
): BaseApiViewModel<UserData, UserModel>(TAG, LoginHelper::userModelToUserData) {
    companion object {
        private const val TAG = "User VM"
    }

    init {
        viewModelScope.launch {
            userRepository.localUser.collectLatest {
                if (it == null)
                    return@collectLatest

                AppLogger.log(tag, "Local user changed: $it")
                _data.value = mapper(it)
            }
        }

        updateUser()
    }

    fun updateUser() {
        AppLogger.log(tag, "Get user")
        fetchData { userRepository.updateUser() }
    }
}