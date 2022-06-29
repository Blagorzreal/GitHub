package com.example.github.data.remote.auth

import com.example.github.data.remote.ResponseResult
import com.example.github.model.UserModel

class MockedAuthApi: IAuthApi {
    override suspend fun login(username: String): ResponseResult<UserModel> =
        ResponseResult.Success(UserModel(0, username, 8, 6, null))
}