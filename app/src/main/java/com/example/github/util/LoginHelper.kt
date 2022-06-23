package com.example.github.util

import com.example.github.data.data.UserData
import com.example.github.data.remote.ResponseResult
import com.example.github.model.UserModel
import com.example.github.ui.screen.login.UsernameValidationError
import com.example.github.util.Constants.Companion.HTTP_ERROR_NOT_FOUND

class LoginHelper private constructor() {
    companion object {
        private val USER_NAME_VALIDATION_REGEX = "^[A-Za-z][A-Za-z0-9_]{0,50}$".toRegex()

        fun isUsernameNotFoundError(responseError: ResponseResult.ResponseError) =
            (responseError is ResponseResult.UnsuccessfulResponseError) &&
                    (responseError.code == HTTP_ERROR_NOT_FOUND)

        fun validateUsername(username: String): UsernameValidationError? {
            if (username.isBlank())
                return UsernameValidationError.EmptyUsernameValidationError

            if (!USER_NAME_VALIDATION_REGEX.matches(username))
                return UsernameValidationError.BadUsernameValidationError

            return null
        }

        fun userModelToUserData(userModel: UserModel) =
            UserData(
                userModel.id,
                userModel.login,
                userModel.avatarUrl)
    }
}