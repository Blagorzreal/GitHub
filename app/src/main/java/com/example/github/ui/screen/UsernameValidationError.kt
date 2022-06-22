package com.example.github.ui.screen

sealed class UsernameValidationError {
    object EmptyUsernameValidationError: UsernameValidationError()
    object BadUsernameValidationError: UsernameValidationError()
}