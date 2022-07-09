package com.example.github.vm

import com.example.github.data.LoginSession
import com.example.github.data.local.repos.IReposDao
import com.example.github.data.local.user.IUserDao
import com.example.github.util.log.AppLogger
import com.example.github.vm.base.AbstractViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(): AbstractViewModel() {

    sealed class LogoutState {
        object NotLoggedOut: LogoutState()
        object LoggedOut: LogoutState()
        object LoggedOutWithError: LogoutState()
    }

    override val tag = "Profile VM"

    @Inject lateinit var userDao: IUserDao
    @Inject lateinit var reposDao: IReposDao

    private val _isLoggedOut: MutableStateFlow<LogoutState> = MutableStateFlow(LogoutState.NotLoggedOut)
    val isLoggedOut: StateFlow<LogoutState> = _isLoggedOut

    fun forceLogout() {
        logOut(true)
    }

    fun logOut(error: Boolean = false) {
        AppLogger.log(tag, "Log out")

        LoginSession.clean()

        CoroutineScope(dispatcher).launch {
            userDao.deleteAll()
            reposDao.deleteAll()
        }

        if (error)
            _isLoggedOut.value = LogoutState.LoggedOutWithError
        else
            _isLoggedOut.value = LogoutState.LoggedOut
    }
}