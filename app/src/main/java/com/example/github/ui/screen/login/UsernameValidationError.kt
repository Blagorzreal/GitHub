package com.example.github.ui.screen.login

sealed class UsernameValidationError {
    object EmptyUsernameValidationError: UsernameValidationError()
    object BadUsernameValidationError: UsernameValidationError()
}