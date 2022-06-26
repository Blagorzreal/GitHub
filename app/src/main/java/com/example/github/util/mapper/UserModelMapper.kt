package com.example.github.util.mapper

import com.example.github.data.data.UserData
import com.example.github.model.UserModel

class UserModelMapper private constructor() {
    companion object {
        fun userModelToUserData(userModel: UserModel) =
            UserData(
                userModel.id,
                userModel.login,
                userModel.avatarUrl,
                userModel.followers,
                userModel.following)
    }
}