package com.example.github.vm

import com.example.github.data.LoginSession
import com.example.github.data.data.UserData
import com.example.github.data.remote.auth.IAuthApi
import com.example.github.model.UserModel
import com.example.github.util.Constants.Companion.EMPTY_STRING
import com.example.github.util.username.UsernameHelper
import com.example.github.util.log.AppLogger
import com.example.github.util.log.LogType
import com.example.github.util.mapper.UserModelMapper
import com.example.github.util.username.UsernameValidationError
import com.example.github.vm.base.BaseApiViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor()
    : BaseApiViewModel<UserData, UserModel>(UserModelMapper::userModelToUserData) {

    @Inject lateinit var authApi: IAuthApi

    override val tag = "Login VM"

    private val _validationError: MutableStateFlow<UsernameValidationError?> = MutableStateFlow(null)
    val validationError: StateFlow<UsernameValidationError?> = _validationError

    private val _username = MutableStateFlow(EMPTY_STRING)
    val username: StateFlow<String> = _username

    fun onUsernameChanged(username: String) {
        AppLogger.log(tag, "Username changed to $username")
        _username.value = username

        if (validationError.value != null) {
            AppLogger.log(tag, "Reset username error")
            _validationError.value = null
        }
    }

    fun login() {
        AppLogger.log(tag, "login")

        val usernameTrimmed = _username.value.trim().lowercase()

        UsernameHelper.validateUsername(usernameTrimmed)?.let {
            AppLogger.log(tag, "invalid username = $usernameTrimmed", LogType.Warning)
            _validationError.value = it
            return
        }

        fetchData { authApi.login(usernameTrimmed) }
    }

    override fun onData(data: UserData) {
        LoginSession.setUserData(data)
        super.onData(data)
    }
}