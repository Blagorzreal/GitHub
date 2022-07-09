package com.example.github.util.username

sealed class UsernameValidationError {
    object EmptyUsernameValidationError: UsernameValidationError()
    object BadUsernameValidationError: UsernameValidationError()
}