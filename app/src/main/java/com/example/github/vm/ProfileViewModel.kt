package com.example.github.vm

import com.example.github.data.LoginSession
import com.example.github.data.local.DaoProvider
import com.example.github.util.log.AppLogger
import com.example.github.vm.base.AbstractViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel: AbstractViewModel(TAG) {
    private val _isLoggedOut = MutableStateFlow(false)
    val isLoggedOut: StateFlow<Boolean> = _isLoggedOut

    companion object {
        private const val TAG = "Profile VM"
    }

    fun logOut() {
        AppLogger.log(TAG, "Log out")

        LoginSession.clean()

        CoroutineScope(dispatcher).launch {
            DaoProvider.clean()
        }

        _isLoggedOut.value = true
    }
}