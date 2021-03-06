package com.example.github.util.username

class UsernameHelper private constructor() {
    companion object {
        private val USER_NAME_VALIDATION_REGEX = "^[A-Za-z0-9_][A-Za-z0-9_]{0,50}$".toRegex()

        fun validateUsername(username: String): UsernameValidationError? {
            if (username.isBlank())
                return UsernameValidationError.EmptyUsernameValidationError

            if (!USER_NAME_VALIDATION_REGEX.matches(username))
                return UsernameValidationError.BadUsernameValidationError

            return null
        }
    }
}