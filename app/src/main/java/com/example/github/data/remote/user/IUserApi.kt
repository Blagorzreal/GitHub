package com.example.github.data.remote.user

import com.example.github.data.remote.ResponseResult
import com.example.github.model.UserModel

interface IUserApi {
    suspend fun getUser(username: String): ResponseResult<UserModel>
}