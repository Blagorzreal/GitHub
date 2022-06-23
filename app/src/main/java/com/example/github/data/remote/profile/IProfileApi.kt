package com.example.github.data.remote.profile

import com.example.github.data.remote.ResponseResult
import com.example.github.model.RepoModel

interface IProfileApi {
    suspend fun getRepos(username: String): ResponseResult<List<RepoModel>>
}