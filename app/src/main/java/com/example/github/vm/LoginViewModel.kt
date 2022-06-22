package com.example.github.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github.data.data.UserData
import com.example.github.data.remote.ApiProvider
import com.example.github.data.remote.ResponseResult
import com.example.github.data.remote.auth.IAuthApi
import com.example.github.ui.screen.UsernameValidationError
import com.example.github.util.Constants.Companion.EMPTY_STRING
import com.example.github.util.LoginHelper
import com.example.github.util.log.AppLogger
import com.example.github.util.log.LogType
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel(
    private val authApi: IAuthApi = ApiProvider.authApi,
    private val coroutineScope: CoroutineDispatcher = Dispatchers.IO): ViewModel() {

    companion object {
        private const val TAG = "LoginVM"
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _responseError: MutableStateFlow<ResponseResult.ResponseError?> = MutableStateFlow(null)
    val responseError: StateFlow<ResponseResult.ResponseError?> = _responseError

    private val _validationError: MutableStateFlow<UsernameValidationError?> = MutableStateFlow(null)
    val validationError: StateFlow<UsernameValidationError?> = _validationError

    private val _username = MutableStateFlow(EMPTY_STRING)
    val username: StateFlow<String> = _username

    private val _userData: MutableStateFlow<UserData?> = MutableStateFlow(null)
    val userData: StateFlow<UserData?> = _userData

    init {
        AppLogger.log(TAG, "Initialized")
    }

    fun onUsernameChanged(username: String) {
        AppLogger.log(TAG, "Username changed to $username")
        _username.value = username
    }

    fun login() {
        AppLogger.log(TAG, "login")

        val usernameTrimmed = _username.value.trim().lowercase()

        LoginHelper.validateUsername(usernameTrimmed)?.let {
            AppLogger.log(TAG, "invalid username = $usernameTrimmed", logType = LogType.Warning)
            _validationError.value = it
            return
        }

        _isLoading.value = true

        viewModelScope.launch {
            val result = withContext(coroutineScope) {
                authApi.login(usernameTrimmed)
            }

            _isLoading.value = false

            when (result) {
                is ResponseResult.Success -> {
                    AppLogger.log(TAG, "Successfully logged in")
                    _userData.value = LoginHelper.userModelToUserData(result.data)
                }
                is ResponseResult.ResponseError -> {
                    AppLogger.log(TAG, "Unable to login: $result", logType = LogType.Error)
                    _responseError.value = result
                }
            }
        }
    }
}