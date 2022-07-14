package com.example.github.data.remote.repos

import com.example.github.data.remote.ResponseResult
import com.example.github.model.relation.RepoWithOwnerRelation

interface IReposApi {
    suspend fun getRepos(username: String): ResponseResult<List<RepoWithOwnerRelation?>>
}