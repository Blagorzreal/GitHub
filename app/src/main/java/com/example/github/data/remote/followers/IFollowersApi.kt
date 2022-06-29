package com.example.github.data.remote.followers

import com.example.github.data.remote.ResponseResult
import com.example.github.model.UserModel

interface IFollowersApi {
    suspend fun followers(username: String): ResponseResult<List<UserModel>>
}