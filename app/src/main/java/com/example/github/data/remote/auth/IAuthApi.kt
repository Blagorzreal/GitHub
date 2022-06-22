package com.example.github.data.remote.auth

import com.example.github.data.remote.ResponseResult
import com.example.github.model.UserModel

interface IAuthApi {
    suspend fun login(username: String): ResponseResult<UserModel>
}