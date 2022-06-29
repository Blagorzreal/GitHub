package com.example.github.data.remote.user

import com.example.github.data.remote.ResponseResult
import com.example.github.model.UserModel

class MockedUserApi: IUserApi {
    override suspend fun getUser(username: String): ResponseResult<UserModel> =
        ResponseResult.Success(UserModel(58, username, 12, 6, null))
}