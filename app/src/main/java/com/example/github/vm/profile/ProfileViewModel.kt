package com.example.github.vm.profile

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

    override val tag = "Profile VM"

    @Inject lateinit var userDao: IUserDao
    @Inject lateinit var reposDao: IReposDao

    private val _logoutState: MutableStateFlow<LogoutState> = MutableStateFlow(LogoutState.NotLoggedOut)
    val logoutState: StateFlow<LogoutState> = _logoutState

    fun forceLogout() {
        if (_logoutState.value !is LogoutState.NotLoggedOut)
            return

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
            _logoutState.value = LogoutState.LoggedOutWithError(false)
        else
            _logoutState.value = LogoutState.LoggedOut
    }
}