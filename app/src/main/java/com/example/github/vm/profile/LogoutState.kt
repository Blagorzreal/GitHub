package com.example.github.vm.profile

sealed class LogoutState {
    object NotLoggedOut: LogoutState()
    object LoggedOut: LogoutState()
    data class LoggedOutWithError(var handled: Boolean): LogoutState()
}