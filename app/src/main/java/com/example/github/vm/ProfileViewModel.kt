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
class ProfileViewModel @Inject constructor(): AbstractViewModel(TAG) {

    @Inject lateinit var userDao: IUserDao
    @Inject lateinit var reposDao: IReposDao

    private val _isLoggedOut = MutableStateFlow(false)
    val isLoggedOut: StateFlow<Boolean> = _isLoggedOut

    companion object {
        private const val TAG = "Profile VM"
    }

    fun logOut() {
        AppLogger.log(TAG, "Log out")

        LoginSession.clean()

        CoroutineScope(dispatcher).launch {
            userDao.deleteAll()
            reposDao.deleteAll()
        }

        _isLoggedOut.value = true
    }
}