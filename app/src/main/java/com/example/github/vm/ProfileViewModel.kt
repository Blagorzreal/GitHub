package com.example.github.vm

import com.example.github.data.LoginSession
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

    private val _isLoggedOut = MutableStateFlow(false)
    val isLoggedOut: StateFlow<Boolean> = _isLoggedOut

    companion object {
        private const val TAG = "Profile VM"
    }

    fun logOut() {
        AppLogger.log(TAG, "Log out")

        LoginSession.clean()

        CoroutineScope(dispatcher).launch {
        }

        _isLoggedOut.value = true
    }
}