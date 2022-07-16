package com.example.github.vm.profile

import androidx.lifecycle.SavedStateHandle
import com.example.github.data.LoginSession
import com.example.github.data.data.UserData
import com.example.github.data.local.repo.IRepoDao
import com.example.github.data.local.user.IUserDao
import com.example.github.ui.navigation.Route.Companion.USER_DATA
import com.example.github.util.log.AppLogger
import com.example.github.vm.base.AbstractViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(savedStateHandle: SavedStateHandle): AbstractViewModel() {

    val userData by lazy {
        val data = savedStateHandle.get<UserData>(USER_DATA)
        if (data == null)
            forceLogout()

        data
    }

    override val tag = "Profile VM"

    @Inject lateinit var userDao: IUserDao
    @Inject lateinit var repoDao: IRepoDao

    private val _logoutState: MutableStateFlow<LogoutState> = MutableStateFlow(LogoutState.NotLoggedOut)
    val logoutState: StateFlow<LogoutState> = _logoutState

    private fun forceLogout() {
        if (_logoutState.value !is LogoutState.NotLoggedOut)
            return

        logOut(true)
    }

    fun logOut(error: Boolean = false) {
        AppLogger.log(tag, "Log out")

        LoginSession.clean()

        CoroutineScope(dispatcher).launch {
            repoDao.deleteAll()
            userDao.deleteAll()
        }

        if (error)
            _logoutState.value = LogoutState.LoggedOutWithError(false)
        else
            _logoutState.value = LogoutState.LoggedOut
    }
}